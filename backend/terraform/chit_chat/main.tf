provider "aws" {
  region = "${var.aws_region}"
}

data "aws_vpc" "app_cluster" {
  tags {
    cluster = "${var.cluster_name}"
  }
}

data "aws_subnet" "app_cluster" {
  vpc_id            = "${data.aws_vpc.app_cluster.id}"
  state             = "available"
  availability_zone = "${element(split(",", var.aws_availability_zones), count.index)}"

  tags {
    cluster = "${var.cluster_name}"
  }

  count = "${length(split(",", var.aws_availability_zones))}"
}

data "aws_route53_zone" "organisation" {
  name = "monarchsofcoding.com."
}

### ECS ChitChat App containers
data "template_file" "ecs_chit-chat_def" {
  template = "${file("${path.module}/chit-chat-def.tpl.json")}"

  vars {
    node_cookie        = "${var.secret_key_base}"
    secret_key_base    = "${var.secret_key_base}"
    guadian_secret_key = "${var.guardian_secret_key}"
    environment        = "${var.environment}"
    database_username  = "chit-chat_${var.environment}"
    database_password  = "${var.database_password}"
    database_name      = "chit-chat_${var.environment}"

    domain = "${var.domain}"

    backend_version = "${var.container_version}"

    cloudwatch_log_group = "${aws_cloudwatch_log_group.chit_chat.arn}"
    cloudwatch_region    = "${var.aws_region}"

    weave_cidr = "${var.weave_cidr}"
  }
}

resource "aws_ecs_task_definition" "chit_chat" {
  family                = "chit-chat_${var.environment}"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name            = "chit-chat_${var.environment}"
  cluster         = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count   = "${var.num_of_containers}"
  iam_role        = "${aws_iam_role.ecs_service.arn}"

  placement_strategy {
    type  = "spread"
    field = "attribute:ecs.availability-zone"
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.front_end.id}"
    container_name   = "chit-chat_${var.environment}"
    container_port   = "80"
  }

  depends_on = [
    "aws_iam_role_policy.ecs_service",
    "aws_alb_listener.front_end",
  ]
}

#### Log Group for ChitChat App
resource "aws_cloudwatch_log_group" "chit_chat" {
  name = "${var.environment}.chit-chat-container-logs"

  retention_in_days = 7

  tags {
    Name        = "Chit Chat"
    Environment = "${var.environment}"
  }
}

### ECS Postgres containers
data "template_file" "ecs_postgres_def" {
  template = "${file("${path.module}/postgres-def.tpl.json")}"

  vars {
    db_username = "chit-chat_${var.environment}"
    db_password = "${var.database_password}"
    db_name     = "chit-chat_${var.environment}"
    environment = "${var.environment}"
    weave_cidr = "${var.weave_cidr}"
  }
}

resource "aws_ecs_task_definition" "postgres" {
  family                = "postgres_${var.environment}"
  container_definitions = "${data.template_file.ecs_postgres_def.rendered}"
}

resource "aws_ecs_service" "postgres" {
  name            = "postgres_${var.environment}"
  cluster         = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.postgres.arn}"
  desired_count   = 1

  placement_strategy {
    type  = "binpack"
    field = "cpu"
  }
}

resource "aws_route53_record" "domain" {
  zone_id = "${data.aws_route53_zone.organisation.zone_id}"
  name    = "${var.domain}"
  type    = "A"

  alias {
    name                   = "${aws_alb.front_end.dns_name}"
    zone_id                = "${aws_alb.front_end.zone_id}"
    evaluate_target_health = false
  }
}

resource "aws_alb" "front_end" {
  name            = "chit-chat-${var.environment}-alb"
  subnets         = ["${data.aws_subnet.app_cluster.*.id}"]
  security_groups = ["${aws_security_group.alb_sg.id}"]

  provisioner "local-exec" {
    command = "sleep 10"
  }
}

resource "aws_alb_target_group" "front_end" {
  name     = "chit-chat-${var.environment}-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "${data.aws_vpc.app_cluster.id}"

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 3
    protocol            = "HTTP"
    interval            = 5
    matcher             = "200,404"
  }
}

resource "aws_security_group" "alb_sg" {
  description = "Controls access to and from the ALB"

  vpc_id = "${data.aws_vpc.app_cluster.id}"
  name   = "chit-chat.${var.environment}.alb-sg"

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
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

resource "aws_iam_role" "ecs_service" {
  name = "chit-chat.${var.environment}.ecs_role"

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
  name = "chit-chat.${var.environment}.ecs_policy"
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

data "aws_acm_certificate" "chit_chat" {
  domain   = "chitchat.monarchsofcoding.com"
  statuses = ["ISSUED"]
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.front_end.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.front_end.id}"
    type             = "forward"
  }
}

resource "aws_alb_listener" "front_end_ssl" {
  load_balancer_arn = "${aws_alb.front_end.id}"
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2015-05"
  certificate_arn   = "${data.aws_acm_certificate.chit_chat.arn}"

  default_action {
    target_group_arn = "${aws_alb_target_group.front_end.id}"
    type             = "forward"
  }
}
