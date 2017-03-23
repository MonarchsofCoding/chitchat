module "main" {
  source = "../../chit_chat"

  aws_region = "eu-west-1"

  cluster_name = "monarchsofcoding"

  environment = "production"
  domain      = "chitchat.monarchsofcoding.com"

  container_version = "${var.container_version}"
  weave_cidr = "10.32.102.0/24"
  num_of_containers = 4

  secret_key_base     = "${var.secret_key_base}"
  database_password   = "${var.database_password}"
  guardian_secret_key = "${var.guardian_secret_key}"

  aws_availability_zones = "eu-west-1a,eu-west-1b,eu-west-1c"
}
