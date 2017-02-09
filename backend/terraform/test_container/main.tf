data "template_file" "ecs_chit-chat_def" {
  template = "${file("test-def.tpl.json")}"
}

resource "aws_ecs_task_definition" "chit_chat" {
  family = "test_hello"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name = "test-hello"
  cluster = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count = 1

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }
}

resource "aws_route53_record" "test" {

  zone_id = "${var.zone_id}"
  name = "test.monarchsofcoding.com"
  type = "CNAME"

  ttl = 600
  records = ["monarchsofcoding.com"]
}
