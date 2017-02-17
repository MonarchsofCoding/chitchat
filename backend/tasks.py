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
  Builds a Docker container for the Backend
  """
  __check_branch()
  git = vcs.Git()

  version = git.get_version()

  lxc.Docker.build(cli,
      dockerfile='Dockerfile.app',
      tag="monarchsofcoding/chitchat:{0}".format(version)
  )

  lxc.Docker.login(cli)

  lxc.Docker.push(cli, ["monarchsofcoding/chitchat:{0}".format(version)])

@task
def deploy(ctx):
  """
  Deploys container to AWS ECS
  """
  env_dir = __check_branch()

  cli.pull("articulate/terragrunt", "0.8.6")

  git = vcs.Git()
  version = git.get_version()

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.6",
    command="apply",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": os.getenv("{0}_DB_PASSWORD".format(env_dir)),
      "TF_VAR_secret_key_base": os.getenv("{0}_SECRET_KEY_BASE".format(env_dir)),
      "TF_VAR_guardian_secret_key": os.getenv("{0}_GUARDIAN_SECRET_KEY".format(env_dir)),
      "TF_VAR_container_version": version
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/{0}".format(env_dir)
  )

@task
def test(ctx):
    """
    Tests the ChitChat Backend
    """

    lxc.Docker.build(cli,
        dockerfile='Dockerfile.dev',
        tag="{0}-dev".format("chitchat-backend")
    )

    cli.pull("postgres", "latest")

    postgres_container = lxc.Docker.run(
      cli,
      'postgres',
      command="",
      environment={
        "POSTGRES_PASSWORD": "postgres",
        "POSTGRES_USER": "postgres",
        "POSTGRES_DB": "chit_chat_test"
      },
      detach=True
    )

    import time
    time.sleep(10)

    setup = "mix local.hex --force && mix local.rebar --force && mix deps.get"
    tests = "mix test --color --trace"
    coverage = "mix coveralls.html --color"
    lint = "mix credo --strict"

    try:
      lxc.Docker.run(cli,
          tag="{0}-dev".format("chitchat-backend"),
          command='/bin/sh -c "{0} && {1} && {2}; {3}"'.format(setup, tests, coverage, lint),
          volumes=[
              "{0}/chit_chat:/app".format(os.getcwd())
          ],
          working_dir="/app",
          environment={
            "TERM": "xterm-color",
            "MIX_ENV": "test"
          },
          links={
            postgres_container.get('Id'): "postgres"
          }
      )

    finally:
      cli.stop(postgres_container.get('Id'))
      cli.remove_container(postgres_container.get('Id'))


@task
def publish_test_artifacts(ctx):
  cli.pull("garland/aws-cli-docker", "latest")

  s3_artifacts = "s3://kcl-chit-chat-artifacts/builds/{0}/backend".format(os.getenv("TRAVIS_BUILD_NUMBER"))

  local_coverage_html = "cover/excoveralls.html"

  lxc.Docker.run(cli,
    tag="garland/aws-cli-docker:latest",
    command='aws s3 cp {0} {1}/coverage/index.html'.format(local_coverage_html, s3_artifacts),
    volumes=[
      "{0}/chit_chat:/app".format(os.getcwd()),
    ],
    working_dir="/app",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "AWS_DEFAULT_REGION": "eu-west-1"
    }
  )
