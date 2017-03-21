# Configure Terragrunt to use DynamoDB for locking
terragrunt = {
  lock = {
    backend = "dynamodb"

    config {
      state_file_id = "kcl-moc-swarm-cluster"
    }
  }

  # Configure Terragrunt to automatically store tfstate files in an S3 bucket
  remote_state = {
    backend = "s3"

    config {
      encrypt = "true"
      bucket  = "monarchs-of-coding-terraform-state"
      key     = "swarm_cluster/terraform.tfstate"
      region  = "eu-west-1"
    }
  }
}

swarm_name = "monarchsofcoding"
