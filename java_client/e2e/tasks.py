from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs

cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

@task
def test(ctx):
  print("Building Backend")
  ctx.run("cd ../../backend && invoke build")
  git = vcs.Git()
  version = git.get_version()

  os.chdir("{0}/../".format(os.getcwd()))
  lxc.Docker.build(cli,
    dockerfile='Dockerfile.dev',
    tag="{0}-dev".format("chitchat-javaclient")
  )
  os.chdir("{0}/e2e/".format(os.getcwd()))

  cli.pull("postgres", "latest")
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

  vnc = "vnc4server -geometry 1920x1080 && export DISPLAY=:1"
  vnc_rec_start = "screen -d -L -m -S vnc2flv flvrec.py -P /vncpasswd -o ChitChatDesktop.flv :1"
  ui_tests = "gradle test --tests com.moc.chitchat.view.*"
  vnc_rec_stop = "screen -X -S vnc2flv kill"
  avconv = "avconv -i ChitChatDesktop.flv -c:v libx264 -crf 19 -strict experimental ChitChatDesktop.mp4"

  try:
    lxc.Docker.run(cli,
      tag="{0}-dev".format("chitchat-javaclient"),
      command='/bin/bash -c "{0} && {1} && {4}; EXIT_CODE=$? && {2} && {3}; exit $EXIT_CODE"'.format(
      vnc, vnc_rec_start, vnc_rec_stop, avconv, ui_tests),
      volumes=[
        "{0}/../ChitChatDesktop:/app".format(os.getcwd())
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
  pass

@task
def publish_test_artifacts(ctx):
  cli.pull("garland/aws-cli-docker", "latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/java_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))
  local_test_video = "ChitChatDesktop.mp4"

  try:
    lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/video/ChitChatDesktop-{2}.mp4'.format(local_test_video, s3_artifacts, os.getenv("TRAVIS_BUILD_NUMBER")),
      volumes=[
        "{0}/../ChitChatDesktop:/app".format(os.getcwd())
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
  pass

@task
def build(ctx):
  pass

@task
def deploy(ctx):
  pass
