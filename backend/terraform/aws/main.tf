provider "aws" {
    region = "eu-west-1"
}

resource "aws_ecs_cluster" "chit_chat" {
  name = "chit-chat"
}

data "template_file" "ecs_chit-chat_def" {
  template = "${file("chit-chat-def.tpl.json")}"
  vars {
    hello = "goodnight"
    world = "moon"
  }
}

data "template_file" "ecs_postgres_def" {
  template = "${file("postgres-def.tpl.json")}"
  vars {
    hello = "goodnight"
    world = "moon"
  }
}

resource "aws_ecs_task_definition" "postgres" {
  family = "postgres"
  container_definitions = "${data.template_file.ecs_postgres_def.rendered}"
}

resource "aws_ecs_service" "postgres" {
  name = "postgres"
  cluster = "${aws_ecs_cluster.chit_chat.id}"
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

resource "aws_ecs_task_definition" "chit_chat" {
  family = "chit-chat"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name = "chit-chat"
  cluster = "${aws_ecs_cluster.chit_chat.id}"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count = 3
  iam_role = "${aws_iam_role.ecs_service.arn}"
  depends_on = ["aws_iam_role_policy.ecs_service"]

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.chit_chat.id}"
    container_name   = "chit-chat"
    container_port   = "80"
  }

  depends_on = [
    "aws_iam_role_policy.ecs_service",
    "aws_alb_listener.front_end",
  ]
}

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

resource "aws_vpc" "chit_chat" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "chit_chat" {
  count             = "3"
  cidr_block        = "${cidrsubnet(aws_vpc.chit_chat.cidr_block, 8, count.index)}"
  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  vpc_id            = "${aws_vpc.chit_chat.id}"
}

resource "aws_internet_gateway" "chit_chat_gateway" {
  vpc_id = "${aws_vpc.chit_chat.id}"
}

resource "aws_route_table" "chit_chat_route_table" {
  vpc_id = "${aws_vpc.chit_chat.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.chit_chat_gateway.id}"
  }
}

resource "aws_route_table_association" "a" {
  count          = "3"
  subnet_id      = "${element(aws_subnet.chit_chat.*.id, count.index)}"
  route_table_id = "${aws_route_table.chit_chat_route_table.id}"
}

## Compute

resource "aws_autoscaling_group" "chit_chat" {
  name                 = "tf-chit-chat-asg"
  vpc_zone_identifier  = ["${aws_subnet.chit_chat.*.id}"]
  min_size             = "1"
  max_size             = "3"
  desired_capacity     = "2"
  launch_configuration = "${aws_launch_configuration.app.name}"
}

resource "aws_launch_configuration" "app" {
  security_groups = [
    "${aws_security_group.instance_sg.id}",
  ]

  image_id                    = "ami-48f9a52e"
  instance_type               = "t2.small"
  associate_public_ip_address = true

  lifecycle {
    create_before_destroy = true
  }
}

## Security Groups

resource "aws_security_group" "alb_sg" {
  description = "Controls access to and from the ALB"

  vpc_id = "${aws_vpc.chit_chat.id}"
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
  vpc_id      = "${aws_vpc.chit_chat.id}"
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
resource "aws_alb_target_group" "chit_chat" {
  name     = "tf-chit-chat-alb-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "${aws_vpc.chit_chat.id}"
}

resource "aws_alb" "chit_chat" {
  name            = "tf-chit-chat-alb-ecs"
  subnets         = ["${aws_subnet.chit_chat.*.id}"]
  security_groups = ["${aws_security_group.alb_sg.id}"]
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.chit_chat.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.chit_chat.id}"
    type             = "forward"
  }
}
