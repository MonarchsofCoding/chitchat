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

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-javaclient"),
        command='/bin/bash -c "vnc4server -geometry 1920x1080 && export DISPLAY=:1 && gradle test"',
        volumes=[
            "{0}/ChitChatDesktop:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-javaclient"),
        command='gradle jacocoTestReport',
        volumes=[
            "{0}/ChitChatDesktop:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )

    lxc.Docker.run(cli,
        tag="{0}-dev".format("chitchat-javaclient"),
        command='gradle check',
        volumes=[
            "{0}/ChitChatDesktop:/app".format(os.getcwd())
        ],
        working_dir="/app",
        environment={}
    )
