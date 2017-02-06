provider "aws" {
    region = "eu-west-1"
}

resource "aws_s3_bucket" "artifacts" {
    bucket = "kcl-chit-chat-artifacts"
    acl = "public-read"

    policy = <<POLICY
{
  "Version":"2012-10-17",
  "Statement":[{
    "Sid":"PublicReadForGetBucketObjects",
        "Effect":"Allow",
      "Principal": "*",
      "Action":"s3:GetObject",
      "Resource":["arn:aws:s3:::kcl-chit-chat-artifacts/*"
      ]
    }
  ]
}
POLICY

    website {
      index_document = "index.html"
    }

    cors_rule {
      allowed_headers = ["*"]
      allowed_methods = ["HEAD","GET"]
      allowed_origins = ["*"]
      expose_headers = ["ETag"]
      max_age_seconds = 3000
    }
}

resource "aws_s3_bucket_object" "index" {
    bucket = "${aws_s3_bucket.artifacts.bucket}"
    key = "index.html"
    source = "artifacts.html"
    content_type = "text/html"
    etag = "${md5(file("artifacts.html"))}"
}
