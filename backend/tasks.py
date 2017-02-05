from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)


@task
def build(ctx):
  """
  Builds a Docker container for the Backend
  """
  lxc.Docker.build(cli,
      dockerfile='Dockerfile.app',
      tag="chitchat-backend"
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

    try:
      lxc.Docker.run(cli,
          tag="{0}-dev".format("chitchat-backend"),
          command='/bin/sh -c "mix local.hex --force && mix local.rebar --force && mix deps.get && mix test --color --trace"',
          volumes=[
              "{0}/chit_chat:/app".format(os.getcwd())
          ],
          working_dir="/app",
          environment={
            "MIX_ENV": "test"
          },
          links={
            postgres_container.get('Id'): "postgres"
          }
      )

      lxc.Docker.run(cli,
         tag="{0}-dev".format("chitchat-backend"),
         command='/bin/sh -c "mix local.hex --force && mix local.rebar --force && mix deps.get && mix coveralls.html"',
         volumes=[
           "{0}/chit_chat:/app".format(os.getcwd())
         ],
         working_dir="/app",
         environment={
          "MIX_ENV": "test"
         },
         links={
           postgres_container.get('Id'): "postgres"
         }
       )

      lxc.Docker.run(cli,
         tag="{0}-dev".format("chitchat-backend"),
         command='/bin/sh -c "mix local.hex --force && mix local.rebar --force && mix deps.get && mix credo"',
         volumes=[
           "{0}/chit_chat:/app".format(os.getcwd())
         ],
         working_dir="/app",
         environment={
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
     working_dir="/app"
   )
