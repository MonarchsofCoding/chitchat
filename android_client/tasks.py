from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

def __check_branch():
  if os.getenv("TRAVIS_PULL_REQUEST") != "false":
    exit("This is a PR, so not deploying.")

  if os.getenv("TRAVIS_BRANCH") == "master":
    return "production"
  elif os.getenv("TRAVIS_BRANCH") == "develop":
    return "beta"
  else:
    exit("Not master or develop, so not deploying.")

@task
def build(ctx):
    """
    Build productionRelease APK
    """
    lxc.Docker.build(cli,
        dockerfile='Dockerfile.dev',
        tag="{0}-dev".format("chitchat-androidclient")
    )

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-androidclient"),
        command='/bin/bash -c "cd app && gradle build"',
        volumes=[
            "{0}/ChitChat:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )
    pass

@task
def deploy(ctx):
  """
  Upload Release to S3
  """
  bin_version = __check_branch()
  cli.pull("garland/aws-cli-docker", "latest")

  s3_binaries = "s3://kcl-chit-chat-artifacts/binaries/{0}/{1}/android_client".format(bin_version, os.getenv("TRAVIS_BUILD_NUMBER"))
  local_apk = "app/build/outputs/apk/app-{0}-release-unsigned.apk".format(bin_version)

  lxc.Docker.run(cli,
    tag="garland/aws-cli-docker:latest",
    command='aws s3 cp {0} {1}/ChitChat-{2}-release-unsigned.apk'.format(local_apk, s3_artifacts, bin_version),
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

  local_coverage = "app/build/JacocoCoverageReport/jacocoTestProductionReleaseUnitTestReport/html/"
  local_tests = "app/build/reports/tests/testProductionReleaseUnitTest/productionRelease/"
  local_lint = "app/build/outputs/lint-results-betaDebug.html"

  try:
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
  except Exception:
    pass

  try:
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
  except Exception:
    pass

  try:
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
  except Exception:
    pass
