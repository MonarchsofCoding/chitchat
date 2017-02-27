provider "aws" {
  region = "${var.aws_region}"
}

## Network
data "aws_availability_zones" "available" {}

# VPC
resource "aws_vpc" "swarm" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags {
    swarm = "${var.swarm_name}"
    Name  = "${var.swarm_name}.swarm"
  }
}

# Subnets
resource "aws_subnet" "swarm" {
  count             = "3"
  cidr_block        = "${cidrsubnet(aws_vpc.swarm.cidr_block, 8, count.index)}"
  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  vpc_id            = "${aws_vpc.swarm.id}"

  tags {
    swarm = "${var.swarm_name}"
    Name  = "${var.swarm_name}.swarm.${count.index}"
  }
}

# Gateway
resource "aws_internet_gateway" "swarm" {
  vpc_id = "${aws_vpc.swarm.id}"
}

# Route Table
resource "aws_route_table" "swarm" {
  vpc_id = "${aws_vpc.swarm.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.swarm.id}"
  }

  tags {
    swarm = "${var.swarm_name}"
    Name  = "${var.swarm_name}.swarm"
  }
}

resource "aws_route_table_association" "swarm" {
  count          = "3"
  subnet_id      = "${element(aws_subnet.swarm.*.id, count.index)}"
  route_table_id = "${aws_route_table.swarm.id}"
}

## Compute
resource "aws_key_pair" "instance" {
  key_name   = "${var.swarm_name}-instance-key"
  public_key = "${file("instance.pub")}"
}

resource "aws_autoscaling_group" "swarm" {
  name                 = "${var.swarm_name}.swarm.asg"
  vpc_zone_identifier  = ["${aws_subnet.swarm.*.id}"]
  min_size             = "1"
  max_size             = "3"
  desired_capacity     = "2"
  launch_configuration = "${aws_launch_configuration.swarm.name}"

  tag {
    key                 = "Name"
    value               = "${var.swarm_name}.swarm"
    propagate_at_launch = true
  }

  tag {
    key                 = "swarm"
    value               = "${var.swarm_name}"
    propagate_at_launch = true
  }
}

data "template_file" "user_data" {
  template = "${file("user-data.sh")}"

  vars {
    aws_region        = "${var.aws_region}"
    ecs_log_level     = "info"
    ecs_agent_version = "latest"
  }
}

resource "aws_launch_configuration" "swarm" {
  security_groups = [
    "${aws_security_group.instance_sg.id}",
  ]

  image_id                    = "ami-405f7226"
  instance_type               = "t2.small"
  associate_public_ip_address = true
  iam_instance_profile        = "${aws_iam_instance_profile.swarm.name}"
  user_data                   = "${data.template_file.user_data.rendered}"
  key_name                    = "${aws_key_pair.instance.id}"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_iam_instance_profile" "swarm" {
  name  = "${var.swarm_name}.swarm-instprofile"
  roles = ["${aws_iam_role.swarm_instance.name}"]
}

// TODO: Reduce AWS Policy to follow Principal of Least Privilege!
resource "aws_iam_role_policy" "swarm_instance" {
  name = "${var.swarm_name}.swarm.policy"
  role = "${aws_iam_role.swarm_instance.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:*",
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "route53:*",
        "route53domains:*",
        "dynamodb:*"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role" "swarm_instance" {
  name = "${var.swarm_name}.swarm.instance-role"

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
  description = "Controls access to nodes in Swarm cluster"
  vpc_id      = "${aws_vpc.swarm.id}"
  name        = "${var.swarm_name}.swarm.sg"

  ingress {
    protocol    = "tcp"
    from_port   = 0
    to_port     = 65535
    cidr_blocks = ["10.0.0.0/16"]
  }

  ingress {
    protocol    = "udp"
    from_port   = 0
    to_port     = 65535
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags {
    swarm = "${var.swarm_name}"
    Name  = "${var.swarm_name}.swarm.instance"
  }
}
