## Backend

### Running locally

Using [docker-compose](https://docs.docker.com/compose/), we can run the backend locally with:
```
docker-compose up
```

When files are changed locally, the backend is automatically rebuilt.

### Invoke tasks
We use invoke tasks to deploy using a containerised Terraform so that we can be sure that same version of Terraform is used whenever we deploy from our machine or Travis.

When using deployment tasks, you will need to have valid AWS Credentials exported to your environment.

If deploying to a different AWS account from Monarchs of Coding, then you will need to change the name S3 bucket used for the Terraform state via Terragrunt. This can be done in the terraform.tfvars in each folder. You will also need to change the

```
data "aws_route53_zone" "organisation" {
  name = "monarchsofcoding.com."
}
```

To your own domain and customize the `cluster_name` in `terraform.tfvars`.

#### Deploying a Cluster
```
invoke create_cluster
```

#### Deploying an Environment
```
invoke deploy --env=<env folder>
```

Refer to the `tasks.py` to see which environment variables are required. Currently they are:

```
export <env_folder>_DB_PASSWORD
export <env_folder>_SECRET_KEY_BASE
export <env_folder>_GUARDIAN_SECRET_KEY
```
