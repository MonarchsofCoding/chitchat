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
  cli.pull("google/cloud-sdk", "latest")

  auth = "gcloud auth activate-service-account --key-file /terraform/travis-gcp-credentials.json"
  gs_artifacts = "gs://kcl-chit-chat-artifacts/builds/{0}/backend".format(os.getenv("TRAVIS_BUILD_NUMBER"))
  acl = "gsutil -m acl ch -Ru AllUsers:R gs://kcl-chit-chat-artifacts"

  local_coverage_html = "cover/excoveralls.html"

  lxc.Docker.run(cli,
     tag="google/cloud-sdk",
     command='/bin/sh -c "{0} && gsutil cp {1} {2}/coverage/index.html && {3}"'.format(
      auth,
      local_coverage_html,
      gs_artifacts,
      acl
     ),
     volumes=[
       "{0}/chit_chat:/app".format(os.getcwd()),
       "{0}/terraform/:/terraform".format(os.getcwd())
     ],
     working_dir="/app"
   )
