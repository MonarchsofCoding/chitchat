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
  cli.pull("google/cloud-sdk", "latest")

  auth = "gcloud auth activate-service-account --key-file /terraform/travis-gcp-credentials.json"
  gs_artifacts = "gs://kcl-chit-chat-artifacts/builds/{0}/android_client".format(os.getenv("TRAVIS_BUILD_NUMBER"))
  acl = "gsutil -m acl ch -Ru AllUsers:R gs://kcl-chit-chat-artifacts"

  local_coverage = "app/build/JacocoCoverageReport/jacocoTestProductionReleaseUnitTestReport/html/*"
  local_tests = "app/build/reports/tests/testProductionReleaseUnitTest/productionRelease/*"
  local_lint = "app/build/outputs/lint-results-debug.html"

  lxc.Docker.run(cli,
     tag="google/cloud-sdk",
     command='/bin/sh -c "{0} && gsutil -m cp -r {1} {2}/coverage/ && gsutil -m cp -r {3} {2}/tests/ && gsutil -m cp -r {4} {2}/lint/index.html && {5}"'.format(
        auth,
        local_coverage,
        gs_artifacts,
        local_tests,
        local_lint,
        acl
     ),
     volumes=[
       "{0}/ChitChat:/app".format(os.getcwd()),
       "{0}/../backend/terraform/:/terraform".format(os.getcwd())
     ],
     working_dir="/app"
   )
