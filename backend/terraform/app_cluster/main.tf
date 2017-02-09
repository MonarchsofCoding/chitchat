provider "aws" {
    region = "${var.aws_region}"
}

## AWS ECS - Elastic Container Service
resource "aws_ecs_cluster" "app" {
  name = "${var.cluster_name}"
}


### ECS IAM Roles
resource "aws_iam_role" "ecs_service" {
  name = "${var.cluster_name}.traefik.ecs_role"

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
  name = "${var.cluster_name}.traefik.ecs_policy"
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

resource "aws_autoscaling_group" "app_cluster" {
  name                 = "${var.cluster_name}.asg"
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
  name  = "${var.cluster_name}.ecs-instprofile"
  roles = ["${aws_iam_role.app_instance.name}"]
}

// TODO: Reduce AWS Policy to follow Principal of Least Privilege!
resource "aws_iam_role_policy" "app_instance" {
  name = "${var.cluster_name}.app_policy"
  role = "${aws_iam_role.app_instance.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:*",
        "ecs:*",
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
  name = "${var.cluster_name}.ecs-instance-role"

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
  name   = "${var.cluster_name}.alb-sg"

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
  name        = "${var.cluster_name}.ecs-sg"

  ingress {
    protocol  = "tcp"
    from_port = 0
    to_port   = 65535

    security_groups = [
      "${aws_security_group.alb_sg.id}"
    ]
  }

  ingress {
    protocol = "tcp"
    from_port = 0
    to_port = 65535
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

## Application Load Balancer to Traefik
resource "aws_alb_target_group" "traefik" {
  name     = "${var.cluster_name}-traefik-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "${aws_vpc.app.id}"

  health_check {
    healthy_threshold = 3
    unhealthy_threshold = 3
    timeout = 3
    protocol = "HTTP"
    interval = 5
    matcher = "200,404"
  }
}

resource "aws_alb" "traefik" {
  name            = "${var.cluster_name}-traefik-alb"
  subnets         = ["${aws_subnet.app.*.id}"]
  security_groups = ["${aws_security_group.alb_sg.id}"]

  provisioner "local-exec" {
    command = "sleep 10"
  }
}

resource "aws_alb_listener" "traefik" {
  load_balancer_arn = "${aws_alb.traefik.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.traefik.id}"
    type             = "forward"
  }
}

### Traefik containers
data "template_file" "ecs_traefik_def" {
  template = "${file("traefik.def.tpl.json")}"
  vars {
    aws_region = "${var.aws_region}"
    cluster_name = "${var.cluster_name}"
    domain_name = "${var.domain_name}"
    cloudwatch_log_group = "${aws_cloudwatch_log_group.traefik.arn}"
    cloudwatch_region = "${var.aws_region}"
  }
}

resource "aws_ecs_task_definition" "traefik" {
  family = "traefik"
  container_definitions = "${data.template_file.ecs_traefik_def.rendered}"
}

resource "aws_ecs_service" "traefik" {
  name = "traefik"
  cluster = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.traefik.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.ecs_service.arn}"

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.traefik.id}"
    container_name   = "traefik"
    container_port   = "80"
  }

  depends_on = [
    "aws_iam_role_policy.ecs_service",
    "aws_alb_listener.traefik",
  ]
}

#### Log Group for Traefik
resource "aws_cloudwatch_log_group" "traefik" {
  name = "${var.cluster_name}.traefik-container-logs"

  retention_in_days = 7

  tags {
    Name = "${var.cluster_name} - Traefik"
  }
}


## Route 53 Domain

resource "aws_route53_zone" "tld" {
   name = "${var.domain_name}"
}

output "zone_id" {
  value = "${aws_route53_zone.tld.zone_id}"
}

resource "aws_route53_record" "alb" {

  zone_id = "${aws_route53_zone.tld.zone_id}"
  name = "${var.domain_name}"
  type = "A"

  alias {
    name = "${aws_alb.traefik.dns_name}"
    zone_id = "${aws_alb.traefik.zone_id}"
    evaluate_target_health = false
  }
}
