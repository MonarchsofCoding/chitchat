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
  Build release JAR
  """
  lxc.Docker.build(cli,
      dockerfile='Dockerfile.dev',
      tag="{0}-dev".format("chitchat-javaclient")
  )

  lxc.Docker.run(cli,
      tag="{0}-dev".format("chitchat-javaclient"),
      command='/bin/bash -c "vnc4server -geometry 1920x1080 && export DISPLAY=:1 && gradle build"',
      volumes=[
          "{0}/ChitChatDesktop:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={}
  )

@task
def deploy(ctx):
  """
  Upload Release to S3
  """
  bin_version = __check_branch()
  cli.pull("garland/aws-cli-docker", "latest")

  s3_binaries = "s3://kcl-chit-chat-artifacts/binaries/{0}/{1}/java_client".format(bin_version, os.getenv("TRAVIS_BUILD_NUMBER"))
  local_jar = "build/libs/app.jar"

  lxc.Docker.run(cli,
    tag="garland/aws-cli-docker:latest",
    command='aws s3 cp {0} {1}/ChitChat.jar'.format(local_jar, s3_binaries),
    volumes=[
        "{0}/ChitChatDesktop:/app".format(os.getcwd())
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
  Tests the ChitChat Desktop
  """

  lxc.Docker.build(cli,
    dockerfile='Dockerfile.dev',
    tag="{0}-dev".format("chitchat-javaclient")
  )

  vnc = "vnc4server -geometry 1920x1080 && export DISPLAY=:1"

  lxc.Docker.run(cli,
    tag="{0}-dev".format("chitchat-javaclient"),
    command='/bin/bash -c "{0} && gradle test && gradle jacocoTestReport && gradle check"'.format(vnc),
    volumes=[
      "{0}/ChitChatDesktop:/app".format(os.getcwd())
    ],
    working_dir="/app",
    environment={}
  )

@task
def publish_test_artifacts(ctx):
  cli.pull("garland/aws-cli-docker", "latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/java_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))

  local_coverage = "build/JacocoCoverageReport/test/html/"
  local_tests = "build/reports/tests/test/"
  local_checkstyle = "build/reports/checkstyle/"

  try:
    lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/coverage/ --recursive'.format(local_coverage, s3_artifacts),
      volumes=[
        "{0}/ChitChatDesktop:/app".format(os.getcwd())
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
        "{0}/ChitChatDesktop:/app".format(os.getcwd())
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
      command='aws s3 cp {0} {1}/checkstyle/ --recursive'.format(local_checkstyle, s3_artifacts),
      volumes=[
        "{0}/ChitChatDesktop:/app".format(os.getcwd())
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
