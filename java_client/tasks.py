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
def build_dev_image(ctx):
  """
  Builds development image to run tests on
  """
  git = vcs.Git()
  version = git.get_version()

  lxc.Docker.build(cli,
      dockerfile='Dockerfile.dev',
      tag="monarchsofcoding/chitchat:desktop-dev-{0}".format(version)
  )

  cli.tag(
    "monarchsofcoding/chitchat:desktop-dev-{0}".format(version),
    "monarchsofcoding/chitchat",
    "desktop-dev"
  )

  lxc.Docker.login(cli)

  lxc.Docker.push(cli, [
    "monarchsofcoding/chitchat:desktop-dev-{0}".format(version),
    "monarchsofcoding/chitchat:desktop-dev"
  ])

@task
def build(ctx):
  """
  Build release JAR
  """
  lxc.Docker.pull(cli, "monarchsofcoding/chitchat:desktop-dev")

  lxc.Docker.run(cli,
      tag="monarchsofcoding/chitchat:desktop-dev",
      command='/bin/bash -c "vnc4server -geometry 1920x1080 && export DISPLAY=:1 && gradle jfxJar && zip -r ChitChatDesktop.zip build/jfx/app"',
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
  local_jar = "ChitChatDesktop.zip"

  lxc.Docker.run(cli,
    tag="garland/aws-cli-docker:latest",
    command='aws s3 cp {0} {1}/ChitChatDesktop.zip'.format(local_jar, s3_binaries),
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
  # lxc.Docker.build(cli,
  #     dockerfile='Dockerfile.dev',
  #     tag="{0}-dev".format("chitchat-javaclient")
  # )

  lxc.Docker.pull(cli, "monarchsofcoding/chitchat:desktop-dev")

  vnc = "vnc4server -geometry 1920x1080 && export DISPLAY=:1"
  tests = "gradle test"
  static_analysis = "gradle jacocoTestReport; gradle check -x test"

  lxc.Docker.run(cli,
    tag="monarchsofcoding/chitchat:desktop-dev",
    command='/bin/bash -c "{0} && {1} && {2}"'.format(
    vnc, tests, static_analysis),
    volumes=[
      "{0}/ChitChatDesktop:/app".format(os.getcwd())
    ],
    working_dir="/app",
    environment={
      "CHITCHAT_ENV": "test"
    }
  )

@task
def publish_test_artifacts(ctx):
  lxc.Docker.pull(cli, "garland/aws-cli-docker:latest")

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
