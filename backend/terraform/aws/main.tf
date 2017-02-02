provider "aws" {
    region = "${var.aws_region}"
}

## AWS ECS - Elastic Container Service

resource "aws_ecs_cluster" "app" {
  name = "${var.cluster_name}"
}

### ECS ChitChat App containers

data "template_file" "ecs_chit-chat_def" {
  template = "${file("chit-chat-def.tpl.json")}"
  vars {
    secret_key_base = "${var.backend_secret_key_base}"
    database_username = "${var.backend_database_username}"
    database_password = "${var.backend_database_password}"
    database_name = "${var.backend_database_name}"
  }
}

resource "aws_ecs_task_definition" "chit_chat" {
  family = "chit-chat"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name = "chit-chat"
  cluster = "${aws_ecs_cluster.app.id}"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count = 3
  iam_role = "${aws_iam_role.ecs_service.arn}"

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.app.id}"
    container_name   = "chit-chat"
    container_port   = "80"
  }

  depends_on = [
    "aws_iam_role_policy.ecs_service",
    "aws_alb_listener.front_end",
  ]
}

### ECS Postgres containers

data "template_file" "ecs_postgres_def" {
  template = "${file("postgres-def.tpl.json")}"
  vars {
    db_username = "${var.backend_database_username}"
    db_password = "${var.backend_database_password}"
    db_name = "${var.backend_database_name}"
  }
}

resource "aws_ecs_task_definition" "postgres" {
  family = "postgres"
  container_definitions = "${data.template_file.ecs_postgres_def.rendered}"
}

resource "aws_ecs_service" "postgres" {
  name = "postgres"
  cluster = "${aws_ecs_cluster.app.id}"
  task_definition = "${aws_ecs_task_definition.postgres.arn}"
  desired_count = 1
  /*iam_role = "${aws_iam_role.ecs_service.arn}"*/
  depends_on = ["aws_iam_role_policy.ecs_service"]

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

  /*load_balancer {
    elb_name = "${aws_elb.foo.name}"
    container_name = "mongo"
    container_port = 8080
  }*/
}

### ECS IAM Roles

resource "aws_iam_role" "ecs_service" {
  name = "tf_example_ecs_role"

  assume_role_policy = <<EOF
{
  "Version": "2008-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "ecs_service" {
  name = "tf_example_ecs_policy"
  role = "${aws_iam_role.ecs_service.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:Describe*",
        "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
        "elasticloadbalancing:DeregisterTargets",
        "elasticloadbalancing:Describe*",
        "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
        "elasticloadbalancing:RegisterTargets"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

## Network

data "aws_availability_zones" "available" {}

resource "aws_vpc" "app" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support = true
  enable_dns_hostnames = true
}

resource "aws_subnet" "app" {
  count             = "3"
  cidr_block        = "${cidrsubnet(aws_vpc.app.cidr_block, 8, count.index)}"
  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  vpc_id            = "${aws_vpc.app.id}"
}

resource "aws_internet_gateway" "app" {
  vpc_id = "${aws_vpc.app.id}"
}

resource "aws_route_table" "app" {
  vpc_id = "${aws_vpc.app.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.app.id}"
  }
}

resource "aws_route_table_association" "a" {
  count          = "3"
  subnet_id      = "${element(aws_subnet.app.*.id, count.index)}"
  route_table_id = "${aws_route_table.app.id}"
}

## Route53
resource "aws_route53_zone" "service_discovery" {
  name = "servicediscovery.internal"
  vpc_id = "${aws_vpc.app.id}"
  force_destroy = true
}

## Compute

resource "aws_autoscaling_group" "chit_chat" {
  name                 = "tf-chit-chat-asg"
  vpc_zone_identifier  = ["${aws_subnet.app.*.id}"]
  min_size             = "1"
  max_size             = "3"
  desired_capacity     = "2"
  launch_configuration = "${aws_launch_configuration.app.name}"
}

data "template_file" "user_data" {
  template = "${file("user-data.sh")}"

  vars {
    aws_region         = "${var.aws_region}"
    ecs_cluster_name   = "${aws_ecs_cluster.app.name}"
    ecs_log_level      = "info"
    ecs_agent_version  = "latest"
  }
}

output "user_data" {
  value = "${data.template_file.user_data.rendered}"
}

resource "aws_launch_configuration" "app" {
  security_groups = [
    "${aws_security_group.instance_sg.id}",
  ]

  image_id                    = "ami-48f9a52e"
  instance_type               = "t2.small"
  associate_public_ip_address = true
  iam_instance_profile        = "${aws_iam_instance_profile.app.name}"
  user_data = "${data.template_file.user_data.rendered}"

  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    "aws_ecs_cluster.app"
  ]
}

resource "aws_iam_instance_profile" "app" {
  name  = "tf-ecs-instprofile"
  roles = ["${aws_iam_role.app_instance.name}"]
}

resource "aws_iam_role_policy" "app_instance" {
  name = "tf_app_policy"
  role = "${aws_iam_role.app_instance.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecs:CreateCluster",
        "ecs:DeregisterContainerInstance",
        "ecs:DiscoverPollEndpoint",
        "ecs:Poll",
        "ecs:RegisterContainerInstance",
        "ecs:StartTelemetrySession",
        "ecs:Submit*",
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "route53:*",
        "route53domains:*"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role" "app_instance" {
  name = "tf-ecs-example-instance-role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

## Security Groups

resource "aws_security_group" "alb_sg" {
  description = "Controls access to and from the ALB"

  vpc_id = "${aws_vpc.app.id}"
  name   = "tf-chit-chat-alb-sg"

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"

    cidr_blocks = [
      "0.0.0.0/0",
    ]
  }
}

resource "aws_security_group" "instance_sg" {
  description = "Controls access to nodes in ECS cluster"
  vpc_id      = "${aws_vpc.app.id}"
  name        = "tf-chit-chat-ecs-sg"

  ingress {
    protocol  = "tcp"
    from_port = 80
    to_port   = 80

    security_groups = [
      "${aws_security_group.alb_sg.id}",
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

## Application Load Balancer
resource "aws_alb_target_group" "app" {
  name     = "tf-chit-chat-alb-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "${aws_vpc.app.id}"
}

resource "aws_alb" "app" {
  name            = "tf-chit-chat-alb-ecs"
  subnets         = ["${aws_subnet.app.*.id}"]
  security_groups = ["${aws_security_group.alb_sg.id}"]

  provisioner "local-exec" {
    command = "sleep 10"
  }
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.app.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.app.id}"
    type             = "forward"
  }
}
