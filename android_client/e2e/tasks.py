from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

@task
def build(ctx):
    pass

@task
def deploy(ctx):
  pass

def find_image(repository, tag):
  print("# Looking for {0}:{1}...".format(repository, tag))
  imgs = cli.images(name=repository)
  git = vcs.Git()
  version = git.get_version()

  for img in imgs:
    if img['RepoTags']:
      for t in img['RepoTags']:
        if t == "{0}:{1}".format(repository, tag):
          print("# Found {0}:{1}".format(repository, tag))
          return True

  print("# Could not find {0}:{1}".format(repository, tag))
  return False

@task
def test(ctx):
    """
    Tests the ChitChat Android client
    """
    git = vcs.Git()
    version = git.get_version()

    if not find_image("monarchsofcoding/chitchat", "release-{0}".format(version)):
      print("Building Backend")
      ctx.run("cd ../../backend && invoke build")

    # os.chdir("{0}/../".format(os.getcwd()))
    # lxc.Docker.build(cli,
    #   dockerfile='Dockerfile.dev',
    #   tag="{0}-dev".format("chitchat-androidclient")
    # )
    # os.chdir("{0}/e2e/".format(os.getcwd()))

    lxc.Docker.pull(cli, "monarchsofcoding/chitchat:android-dev")

    lxc.Docker.pull(cli, "postgres:latest")
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

    backend_container = lxc.Docker.run(
      cli,
      "monarchsofcoding/chitchat:release-{0}".format(version),
      command='foreground',
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

    preload_classes = "gradle compileUiTestReleaseSources"
    start_emulator = "screen -d -L -m -S emulator emulator64-x86 -avd nougat -noaudio -no-window -gpu off -verbose -qemu -vnc :1"
    print_screen_log = "sleep 5; cat screenlog.0"
    vnc_rec_start = "{0} && android-wait-for-emulator && screen -d -L -m -S vnc2flv flvrec.py -o ChitChatAndroid.flv :1".format(print_screen_log)
    instrumented_tests = "gradle connectedUiTestsDebugAndroidTest"
    vnc_rec_stop = "screen -X -S vnc2flv kill"
    stop_emulator = "screen -X -S emulator kill"
    avconv = "avconv -i ChitChatAndroid.flv -c:v libx264 -crf 19 -strict experimental ChitChatAndroid.mp4"

    try:
      lxc.Docker.run(cli,
          tag="monarchsofcoding/chitchat:android-dev",
          command='/bin/bash -c "cd app && {0} && {1}; {2} && {3}; EXIT_CODE=$? && {4}; {5}; {6}; exit $EXIT_CODE"'.format(
            preload_classes, start_emulator, vnc_rec_start, instrumented_tests, vnc_rec_stop, stop_emulator, avconv
          ),
          volumes=[
              "{0}/../ChitChat:/app".format(os.getcwd())
          ],
          working_dir="/app",
          environment={},
          links={
            backend_container.get('Id'): "chitchat"
          },
          privileged=True
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
  lxc.Docker.pull(cli, "garland/aws-cli-docker:latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/android_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))

  local_test_video = "app/ChitChatAndroid.mp4"

  try:
    lxc.Docker.run(cli,
      tag="garland/aws-cli-docker:latest",
      command='aws s3 cp {0} {1}/video/ChitChatAndroid-{2}.mp4'.format(local_test_video, s3_artifacts, os.getenv("TRAVIS_BUILD_NUMBER")),
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
