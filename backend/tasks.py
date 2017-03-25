from invoke import task
from docker import Client
import os
import shutil
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

def __check_branch():
  if os.getenv("TRAVIS_PULL_REQUEST") != "false":
    exit("This is a PR, so not deploying.")

  if os.getenv("TRAVIS_BRANCH") == "master":
    return "production"
  elif os.getenv("TRAVIS_BRANCH") == "develop":
    return "beta"

  exit("Not on master or develop")

@task
def build(ctx):
  """
  Builds a Docker container for the Backend
  """
  lxc.Docker.clean(cli, [
    "chit_chat/_build",
    "chit_chat/deps"
  ])
  git = vcs.Git()
  version = git.get_version()

  # TODO: Upgrade Phoenix as soon as it supports poision 3.0. Doing this stuff isn't pleasant.
  fix_gossip = "sed -i 's#\[opts\]#opts#' deps/libcluster/lib/strategy/gossip.ex"

  lxc.Docker.build(cli,
    dockerfile='Dockerfile.dev',
    tag="{0}-dev".format("chitchat-backend")
  )

  lxc.Docker.run(cli,
    tag="{0}-dev".format("chitchat-backend"),
    command='/bin/sh -c "mix do deps.get && {0} && mix deps.compile && mix release --env=prod --verbose"'.format(fix_gossip),
    volumes=[
      "{0}/chit_chat:/app".format(os.getcwd())
    ],
    working_dir="/app",
    environment={
      "TERM": "xterm",
      "MIX_ENV": "prod"
    }
  )

  src = "chit_chat/_build/prod/rel/{0}/releases/{1}/{0}.tar.gz".format("chit_chat", "0.0.1")
  shutil.copyfile(src, "{0}.tar.gz".format("chit_chat"))

  lxc.Docker.build(cli,
    dockerfile='Dockerfile.app',
    tag="monarchsofcoding/chitchat:release-{0}".format(version)
  )

  cli.tag(
    "monarchsofcoding/chitchat:release-{0}".format(version),
    "monarchsofcoding/chitchat",
    "release"
  )


@task
def deploy(ctx, env=None):
  """
  Deploys a given environment (or detects on Travis) to the cluster.
  """
  if not env:
    env = __check_branch()

  lxc.Docker.pull(cli, "articulate/terragrunt:0.8.8")

  git = vcs.Git()
  version = git.get_version()

  lxc.Docker.login(cli)
  lxc.Docker.push(cli, [
    "monarchsofcoding/chitchat:release-{0}".format(version),
    "monarchsofcoding/chitchat:release"
  ])

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="get",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": os.getenv("{0}_DB_PASSWORD".format(env)),
      "TF_VAR_secret_key_base": os.getenv("{0}_SECRET_KEY_BASE".format(env)),
      "TF_VAR_guardian_secret_key": os.getenv("{0}_GUARDIAN_SECRET_KEY".format(env)),
      "TF_VAR_container_version": version
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/{0}".format(env)
  )

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="apply",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": os.getenv("{0}_DB_PASSWORD".format(env)),
      "TF_VAR_secret_key_base": os.getenv("{0}_SECRET_KEY_BASE".format(env)),
      "TF_VAR_guardian_secret_key": os.getenv("{0}_GUARDIAN_SECRET_KEY".format(env)),
      "TF_VAR_container_version": version
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/{0}".format(env)
  )
  pass

@task
def destroy(ctx, env):
  """
  Destroys a given environment on the cluster.
  """
  env_dir = env

  lxc.Docker.pull(cli, "articulate/terragrunt:0.8.8")

  git = vcs.Git()
  version = git.get_version()

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="get",
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

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="destroy --force",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": "destroy",
      "TF_VAR_secret_key_base": "destroy",
      "TF_VAR_guardian_secret_key": "destroy",
      "TF_VAR_container_version": "destroy"
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/{0}".format(env_dir)
  )
  pass

@task
def create_cluster(ctx):
  """
  Creates the underlying cluster for running containers.
  """
  lxc.Docker.pull(cli, "articulate/terragrunt:0.8.8")

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="apply",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/weave_cluster"
  )
  pass

@task
def destroy_cluster(ctx):
  """
  Destroys the underlying cluster for running containers.
  """
  lxc.Docker.pull(cli, "articulate/terragrunt:0.8.8")

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="destroy --force",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/weave_cluster"
  )
  pass

@task
def test(ctx):
  """
  Tests the ChitChat Backend
  """

  lxc.Docker.build(cli,
    dockerfile='Dockerfile.dev',
    tag="{0}-dev".format("chitchat-backend")
  )

  lxc.Docker.pull(cli, "postgres:latest")

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

  setup = "mix deps.get"
  wait = "/bin/wait-for-it.sh -t 120 postgres:5432"
  tests = "mix test --color --trace"
  coverage = "mix coveralls.html --color"
  lint = "mix credo --strict"
  dogma = "mix dogma"

  try:
    lxc.Docker.run(cli,
      tag="{0}-dev".format("chitchat-backend"),
      command='/bin/sh -c "{0} && {1} && {2} && {3} && {4} && {5}"'.format(
        setup, wait, tests, coverage, lint, dogma),
      volumes=[
        "{0}/chit_chat:/app".format(os.getcwd())
      ],
      working_dir="/app",
      environment={
        "TERM": "xterm",
        "MIX_ENV": "test"
      },
      links={
        postgres_container.get('Id'): "postgres"
      }
    )

  finally:
    cli.stop(postgres_container.get('Id'))
    cli.remove_container(postgres_container.get('Id'))

  if os.getenv("CI") == "true":
    test_infra(ctx)


@task
def test_infra(ctx):
  """
  Tests that the terraform code is deployable by performing a plan.
  """
  lxc.Docker.pull(cli, "articulate/terragrunt:0.8.8")

  git = vcs.Git()
  version = git.get_version()

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="get",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": "test",
      "TF_VAR_secret_key_base": "test",
      "TF_VAR_guardian_secret_key": "test",
      "TF_VAR_container_version": version
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/alpha"
  )

  terragrunt_container = lxc.Docker.run(cli,
    "articulate/terragrunt:0.8.8",
    command="plan",
    environment={
      "AWS_ACCESS_KEY_ID": os.getenv("AWS_ACCESS_KEY_ID"),
      "AWS_SECRET_ACCESS_KEY": os.getenv("AWS_SECRET_ACCESS_KEY"),
      "TF_VAR_database_password": "test",
      "TF_VAR_secret_key_base": "test",
      "TF_VAR_guardian_secret_key": "test",
      "TF_VAR_container_version": version
    },
    volumes=[
      "{0}/terraform:/app".format(os.getcwd())
    ],
    working_dir="/app/environments/alpha"
  )


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
