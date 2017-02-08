data "template_file" "ecs_chit-chat_def" {
  template = "${file("test-def.tpl.json")}"
}

resource "aws_ecs_task_definition" "chit_chat" {
  family = "test_hello"
  container_definitions = "${data.template_file.ecs_chit-chat_def.rendered}"
}

resource "aws_ecs_service" "chat_chat" {
  name = "test-hello"
  cluster = "chit-chat"
  task_definition = "${aws_ecs_task_definition.chit_chat.arn}"
  desired_count = 1

  placement_strategy {
    type = "binpack"
    field = "cpu"
  }
}
