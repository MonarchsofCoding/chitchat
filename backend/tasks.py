from invoke import task
from docker import Client
import os
from invoke_tools import lxc, system, vcs


cli = Client(base_url='unix://var/run/docker.sock', timeout=600)

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
          command='/bin/sh -c "mix local.hex --force && mix local.rebar --force && mix deps.get && mix test"',
          volumes=[
              "{0}/chit_chat:/app".format(os.getcwd())
          ],
          working_dir="/app",
          environment={},
          links={
            postgres_container.get('Id'): "postgres"
          }
      )
    finally:
      cli.stop(postgres_container.get('Id'))
      cli.remove_container(postgres_container.get('Id'))
