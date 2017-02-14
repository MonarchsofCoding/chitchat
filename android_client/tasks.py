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
    lxc.Docker.clean(cli, [
      "ChitChat/app/build",
    ])

    lxc.Docker.build(cli,
        dockerfile='Dockerfile.dev',
        tag="{0}-dev".format("chitchat-androidclient")
    )

    bin_version = __check_branch()
    build_dir = "build/outputs/apk"

    zipalign = "zipalign -v -p 4 {1}/app-{0}-release-unsigned.apk {1}/app-{0}-release-unsigned-aligned.apk".format(bin_version, build_dir)

    sign = "apksigner sign --ks /app/android.jks --ks-key-alias ChitChatKey --ks-pass env:ANDROID_STORE_PASSWORD --key-pass env:ANDROID_KEY_PASSWORD --out {1}/app-{0}-release.apk {1}/app-{0}-release-unsigned-aligned.apk".format(bin_version, build_dir)

    verify = "apksigner verify {1}/app-{0}-release.apk".format(bin_version, build_dir)

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-androidclient"),
        command='/bin/bash -c "cd app && gradle assembleRelease && {0} && {1} && {2}"'.format(zipalign, sign, verify),
        volumes=[
            "{0}/ChitChat:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={
          "ANDROID_KEY_PASSWORD": os.getenv("ANDROID_KEY_PASSWORD"),
          "ANDROID_STORE_FILE": "/app/android.jks",
          "ANDROID_STORE_PASSWORD": os.getenv("ANDROID_STORE_PASSWORD")
        }
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
  local_apk = "app/build/outputs/apk/app-{0}-release.apk".format(bin_version)

  lxc.Docker.run(cli,
    tag="garland/aws-cli-docker:latest",
    command='aws s3 cp {0} {1}/ChitChat-{2}-release.apk'.format(local_apk, s3_binaries, bin_version),
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
