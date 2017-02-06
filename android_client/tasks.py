from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

# system.Info.print_all()

repo = vcs.Git()
# repo.print_all()

@task
def test(ctx):
    """
    Tests the ChitChat Android client
    """

    lxc.Docker.build(cli,
        dockerfile='Dockerfile.dev',
        tag="{0}-dev".format("chitchat-androidclient")
    )

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-androidclient"),
        command='/bin/bash -c "cd app && gradle test && gradle jacocoTestReport && gradle lint"',
        volumes=[
            "{0}/ChitChat:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )

@task
def publish_test_artifacts(ctx):
  cli.pull("garland/aws-cli-docker", "latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/android_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))

  local_coverage = "app/build/JacocoCoverageReport/jacocoTestReleaseUnitTestReport/html/"
  local_tests = "app/build/reports/tests/testReleaseUnitTest/release/"
  local_lint = "app/build/outputs/lint-results-debug.html"

  lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/coverage/ --recursive'.format(local_coverage, s3_artifacts),
      volumes=[
          "{0}/ChitChat:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={
          "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
          "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
          "AWS_DEFAULT_REGION": "eu-west-1"
      }
  )

  lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/tests/ --recursive'.format(local_tests, s3_artifacts),
      volumes=[
          "{0}/ChitChat:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={
          "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
          "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
          "AWS_DEFAULT_REGION": "eu-west-1"
      }
  )

  lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/lint/index.html'.format(local_lint, s3_artifacts),
      volumes=[
          "{0}/ChitChat:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={
          "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
          "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
          "AWS_DEFAULT_REGION": "eu-west-1"
      }
  )
