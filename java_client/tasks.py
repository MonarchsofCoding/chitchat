from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

# system.Info.print_all()

repo = vcs.Git()
# repo.print_all()


@task
def build(ctx):
    """
    Builds the ChitChat Desktop
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
  cli.pull("google/cloud-sdk", "latest")

  auth = "gcloud auth activate-service-account --key-file /terraform/travis-gcp-credentials.json"
  gs_artifacts = "gs://kcl-chit-chat-artifacts/builds/{0}/java_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))
  acl = "gsutil -m acl ch -Ru AllUsers:R gs://kcl-chit-chat-artifacts"

  local_coverage = "build/JacocoCoverageReport/test/html/*"
  local_tests = "build/reports/tests/test/*"
  local_checkstyle = "build/reports/checkstyle/*"

  lxc.Docker.run(cli,
     tag="google/cloud-sdk",
     command='/bin/sh -c "{0} && gsutil -m cp -r {1} {2}/coverage/ && gsutil -m cp -r {3} {2}/tests/ && gsutil -m cp -r {4} {2}/checkstyle/ && {5}"'.format(
        auth,
        local_coverage,
        gs_artifacts,
        local_tests,
        local_checkstyle,
        acl
     ),
     volumes=[
       "{0}/ChitChatDesktop:/app".format(os.getcwd()),
       "{0}/../backend/terraform/:/terraform".format(os.getcwd())
     ],
     working_dir="/app"
   )
