provider "aws" {
  region = "${var.aws_region}"
}

/*data "aws_route53_zone" "organisation" {
  name = "${var.domain_name}."
}*/

## AWS ECS - Elastic Container Service
resource "aws_ecs_cluster" "app" {
  name = "${var.cluster_name}"
}

## Network
data "aws_availability_zones" "available" {}

resource "aws_vpc" "app" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags {
    cluster = "${var.cluster_name}"
    Name    = "${var.cluster_name}.ecs"
  }
}

resource "aws_subnet" "app" {
  count             = "3"
  cidr_block        = "${cidrsubnet(aws_vpc.app.cidr_block, 8, count.index)}"
  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  vpc_id            = "${aws_vpc.app.id}"

  tags {
    cluster = "${var.cluster_name}"
    Name    = "${var.cluster_name}.ecs.${count.index}"
  }
}

output "subnets" {
  value = ["${aws_subnet.app.*.id}"]
}

resource "aws_internet_gateway" "app" {
  vpc_id = "${aws_vpc.app.id}"
}

resource "aws_route_table" "app" {
  vpc_id = "${aws_vpc.app.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.app.id}"
  }

  tags {
    cluster = "${var.cluster_name}"
    Name    = "${var.cluster_name}.ecs"
  }
}

resource "aws_route_table_association" "a" {
  count          = "3"
  subnet_id      = "${element(aws_subnet.app.*.id, count.index)}"
  route_table_id = "${aws_route_table.app.id}"
}

## Route53
resource "aws_route53_zone" "service_discovery" {
  name          = "servicediscovery.internal"
  vpc_id        = "${aws_vpc.app.id}"
  force_destroy = true
}

## Compute

resource "aws_autoscaling_group" "app_cluster" {
  name                 = "${var.cluster_name}.asg"
  vpc_zone_identifier  = ["${aws_subnet.app.*.id}"]
  min_size             = "1"
  max_size             = "3"
  desired_capacity     = "2"
  launch_configuration = "${aws_launch_configuration.app.name}"

  tag {
    key                 = "Name"
    value               = "${var.cluster_name}.ecs"
    propagate_at_launch = true
  }

  tag {
    key                 = "cluster"
    value               = "${var.cluster_name}"
    propagate_at_launch = true
  }
}

data "template_file" "user_data" {
  template = "${file("user-data.sh")}"

  vars {
    aws_region        = "${var.aws_region}"
    ecs_cluster_name  = "${aws_ecs_cluster.app.name}"
    ecs_log_level     = "info"
    ecs_agent_version = "latest"
  }
}

resource "aws_launch_configuration" "app" {
  security_groups = [
    "${aws_security_group.instance_sg.id}",
  ]

  /*image_id                    = "ami-48f9a52e"*/
  image_id                    = "ami-25adf356"
  instance_type               = "t2.small"
  associate_public_ip_address = true
  iam_instance_profile        = "${aws_iam_instance_profile.app.name}"
  user_data                   = "${data.template_file.user_data.rendered}"

  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    "aws_ecs_cluster.app",
  ]
}

resource "aws_iam_instance_profile" "app" {
  name  = "${var.cluster_name}.ecs-instprofile"
  roles = ["${aws_iam_role.app_instance.name}"]
}

// TODO: Reduce AWS Policy to follow Principal of Least Privilege!
resource "aws_iam_role_policy" "app_instance" {
  name = "${var.cluster_name}.app_policy"
  role = "${aws_iam_role.app_instance.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:*",
        "ecs:*",
        "autoscaling:DescribeAutoScalingInstances",
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "route53:*",
        "route53domains:*"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role" "app_instance" {
  name = "${var.cluster_name}.ecs-instance-role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

## Security Groups
resource "aws_security_group" "instance_sg" {
  description = "Controls access to nodes in ECS cluster"
  vpc_id      = "${aws_vpc.app.id}"
  name        = "${var.cluster_name}.ecs-sg"

  ingress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags {
    cluster = "${var.cluster_name}"
    Name    = "${var.cluster_name}.ecs.instance"
  }
}
