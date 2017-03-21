variable "database_password" {
  type        = "string"
  description = "The database password for the application"
}

variable "secret_key_base" {
  type        = "string"
  description = "The secret key base for Phoenix Framework"
}

variable "container_version" {
  type        = "string"
  description = "The version of the container to deploy"
}

variable "guardian_secret_key" {
  type        = "string"
  description = "The secret key for Guardian JWT"
}
