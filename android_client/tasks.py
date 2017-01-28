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
        command='/bin/bash -c "cd app && gradle test"',
        volumes=[
            "{0}/ChitChat:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )

@task
def publish_test_artifacts(ctx):
    # TODO
    pass
