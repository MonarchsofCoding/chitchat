variable "aws_region" {
  type    = "string"
  default = "eu-west-1"
}

variable "cluster_name" {
  type        = "string"
  description = "The name of the cluster"
}

/*variable "domain_name" {
  type        = "string"
  description = "The domain name for the Traefik ALB"
}*/

