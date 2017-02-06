variable "aws_region" {
  type = "string"
  default = "eu-west-1"
}

variable "cluster_name" {
  type = "string"
  description = "The name of the cluster"
}

variable "backend_secret_key_base" {
  type = "string"
  description = "The secret key base for use in the backend Phoenix Framework."
}

variable "backend_database_username" {
  type = "string"
  description = "The username to be created on the database and used by the backend."
}

variable "backend_database_password" {
  type = "string"
  description = "The password to be set on the database user and used by the backend."
}

variable "backend_database_name" {
  type = "string"
  description = "The name of the database to be created and used by the backend."
}

variable "backend_database_ecs_name" {
  type = "string"
  description = "The hostname to resolve when using AWS ECS DNS service discovery"
}
