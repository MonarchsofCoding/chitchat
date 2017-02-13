module "main" {
  source = "../../chit_chat"

  aws_region = "eu-west-1"

  cluster_name = "monarchs-of-coding"

  environment = "beta"
  domain = "beta.chitchat.monarchsofcoding.com"

  container_version = "${var.container_version}"

  secret_key_base = "${var.secret_key_base}"
  database_password = "${var.database_password}"

  aws_availability_zones = "eu-west-1a,eu-west-1b,eu-west-1c"

}