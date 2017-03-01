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

  print("Building Backend")
  # ctx.run("cd ../backend && invoke build")
  git = vcs.Git()
  version = git.get_version()

  postgres_container = lxc.Docker.run(
    cli,
    'postgres',
    command="",
    environment={
      "POSTGRES_PASSWORD": "chit_chat_test",
      "POSTGRES_USER": "chit_chat_test",
      "POSTGRES_DB": "chit_chat_test"
    },
    detach=True
  )

  import time
  time.sleep(5)

  backend_container = lxc.Docker.run(
    cli,
    "monarchsofcoding/chitchat:release-{0}".format(version),
    command='/bin/sh -c "export LOCAL_IP=`hostname -i` && /opt/app/bin/chit_chat foreground"',
    environment={
      "SECRET_KEY_BASE": "G9XBaZMFhtWDxAaowHCBrrDVq8xVB3sfro8xiGnFXfidldnvf",
      "GUARDIAN_SECRET_KEY": "cQCWxKpcuixeB4ZAxCs04nrBGdKeJiHcmmCHbZPI6esGcLcfZVz1qw2796p3gWGA",
      "DATABASE_HOSTNAME": "postgres",
      "DATABASE_USERNAME": "chit_chat_test",
      "DATABASE_PASSWORD": "chit_chat_test",
      "DATABASE_NAME": "chit_chat_test",
      "DATABASE_PORT": "5432",
      "PORT": "4000",
      "MIX_ENV": "dev",
      "NODE_COOKIE": "test"
    },
    detach=True,
    links={
      postgres_container.get('Id'): "postgres"
    }
  )

  lxc.Docker.build(cli,
    dockerfile='Dockerfile.dev',
    tag="{0}-dev".format("chitchat-javaclient")
  )

  vnc = "vnc4server -geometry 1920x1080 && export DISPLAY=:1"
  vnc_rec_start = "screen -d -L -m -S vnc2flv flvrec.py -P /vncpasswd -o ChitChatDesktop.flv :1"
  tests = "gradle test"
  ui_tests = "gradle test --tests com.moc.chitchat.view.*"
  vnc_rec_stop = "screen -X -S vnc2flv kill"
  avconv = "avconv -i ChitChatDesktop.flv -c:v libx264 -crf 19 -strict experimental ChitChatDesktop.mp4"
  static_analysis = "gradle jacocoTestReport; gradle check"

  try:
    lxc.Docker.run(cli,
      tag="{0}-dev".format("chitchat-javaclient"),
      command='/bin/bash -c "{2}; {0} && {1} && {6}; {3} && {4}; {5}"'.format(
      vnc, vnc_rec_start, tests, vnc_rec_stop, avconv, static_analysis, ui_tests),
      volumes=[
        "{0}/ChitChatDesktop:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={
        "CHITCHAT_ENV": "test"
      },
      links={
        backend_container.get('Id'): "chitchat"
      }
    )
  finally:
    backend_logs = cli.logs(backend_container.get('Id'), stdout=True, stderr=True, timestamps=True)
    with open("backend_container.log", "w") as log_file:
      log_file.write(backend_logs.decode("utf-8").strip())

    postgres_logs = cli.logs(postgres_container.get('Id'), stdout=True, stderr=True, timestamps=True)
    with open("postgres_container.log", "w") as log_file:
      log_file.write(postgres_logs.decode("utf-8").strip())

    cli.stop(backend_container.get('Id'))
    cli.remove_container(backend_container.get('Id'))
    cli.stop(postgres_container.get('Id'))
    cli.remove_container(postgres_container.get('Id'))

@task
def publish_test_artifacts(ctx):
  cli.pull("garland/aws-cli-docker", "latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/java_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))

  local_coverage = "build/JacocoCoverageReport/test/html/"
  local_tests = "build/reports/tests/test/"
  local_checkstyle = "build/reports/checkstyle/"
  local_test_video = "ChitChatDesktop.mp4"

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

  try:
    lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/video/ChitChatDesktop-{2}.mp4'.format(local_test_video, s3_artifacts, os.getenv("TRAVIS_BUILD_NUMBER")),
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
