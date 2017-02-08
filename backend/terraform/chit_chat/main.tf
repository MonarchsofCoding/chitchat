### ECS ChitChat Beta App containers
data "template_file" "ecs_chit-chat_def" {
  template = "${file("chit-chat-def.tpl.json")}"
  vars {
    secret_key_base = "${var.secret_key_base}"
    environment = "${var.environment}"
    database_username = "chit-chat_${var.environment}"
    database_password = "${var.database_password}"
    database_name = "chit-chat_${var.environment}"
    ecs_postgres_name = "postgres_${var.environment}.servicediscovery.local"

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
  desired_count = 3
  iam_role = "${aws_iam_role.ecs_service.arn}"

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

  /*load_balancer {
    target_group_arn = "${aws_alb_target_group.app.id}"
    container_name   = "chit-chat_${var.environment}"
    container_port   = "80"
  }*/

  /*depends_on = [
    "aws_iam_role_policy.ecs_service",
    "aws_alb_listener.front_end",
  ]*/
}

#### Log Group for ChitChat App
resource "aws_cloudwatch_log_group" "chit_chat" {
  name = "chit-chat-container-logs"

  retention_in_days = 7

  tags {
    Name = "Chit Chat (${var.environment})"
    Environment = "${var.environment}"
  }
}

### ECS Postgres containers
data "template_file" "ecs_postgres_def" {
  template = "${file("postgres-def.tpl.json")}"
  vars {
    db_username = "chit-chat_${var.environment}"
    db_password = "${var.backend_database_password}"
    db_name = "chit-chat_${var.environment}"
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
  depends_on = ["aws_iam_role_policy.ecs_service"]

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }

}
