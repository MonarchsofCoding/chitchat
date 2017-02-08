module "main" {
  source "../../chit_chat"

  cluster_name = "chit-chat"

  environment = "beta"
  domain = "beta.chitchat.monarchsofcoding.com"

  container_version = "${var.container_version}"

  secret_key_base = "${var.secret_key_base}"
}
