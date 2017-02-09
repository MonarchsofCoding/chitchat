### ECS ChitChat App containers
data "template_file" "ecs_chit-chat_def" {
  template = "${file("${path.module}/chit-chat-def.tpl.json")}"
  vars {
    secret_key_base = "${var.secret_key_base}"
    environment = "${var.environment}"
    database_username = "chit-chat_${var.environment}"
    database_password = "${var.database_password}"
    database_name = "chit-chat_${var.environment}"
    ecs_postgres_name = "postgres_${var.environment}.servicediscovery.internal"
    domain = "${var.domain}"

    backend_version = "${var.container_version}"

    cloudwatch_log_group = "${aws_cloudwatch_log_group.chit_chat.arn}"
    cloudwatch_region = "${var.aws_region}"
  }
}

resource "aws_ecs_task_definition" "chit_chat" {
  family = "chit-chat_${var.environment}"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name = "chit-chat_${var.environment}"
  cluster = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count = 2

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }
}

#### Log Group for ChitChat App
resource "aws_cloudwatch_log_group" "chit_chat" {
  name = "${var.environment}.chit-chat-container-logs"

  retention_in_days = 7

  tags {
    Name = "Chit Chat"
    Environment = "${var.environment}"
  }
}

### ECS Postgres containers
data "template_file" "ecs_postgres_def" {
  template = "${file("${path.module}/postgres-def.tpl.json")}"
  vars {
    db_username = "chit-chat_${var.environment}"
    db_password = "${var.database_password}"
    db_name = "chit-chat_${var.environment}"
    environment = "${var.environment}"
  }
}

resource "aws_ecs_task_definition" "postgres" {
  family = "postgres_${var.environment}"
  container_definitions = "${data.template_file.ecs_postgres_def.rendered}"
}

resource "aws_ecs_service" "postgres" {
  name = "postgres_${var.environment}"
  cluster = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.postgres.arn}"
  desired_count = 1

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

}

resource "aws_route53_record" "domain" {

  zone_id = "${var.zone_id}"
  name = "${var.domain}"
  type = "CNAME"

  ttl = 600
  records = ["${var.traefik_alb_domain}"]
}
