cluster_name = "monarchsofcoding"

terragrunt = {
  # Configure Terragrunt to use DynamoDB for locking
  lock {
    backend = "dynamodb"

    config {
      state_file_id = "kcl-moc-weave-cluster"
    }
  }

  # Configure Terragrunt to automatically store tfstate files in an S3 bucket
  remote_state {
    backend = "s3"

    config {
      encrypt = "true"
      bucket  = "monarchs-of-coding-terraform-state"
      key     = "weave_cluster/terraform.tfstate"
      region  = "eu-west-1"
    }
  }
}
