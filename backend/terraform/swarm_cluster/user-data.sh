#!/bin/bash

# Install Docker
apt-get install -y --no-install-recommends \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common

curl -fsSL https://apt.dockerproject.org/gpg | sudo apt-key add -

add-apt-repository \
       "deb https://apt.dockerproject.org/repo/ \
       ubuntu-$(lsb_release -cs) \
       main"

apt-get update -y
apt-get -y install docker-engine python3 python3-pip

systemctl enable docker
systemctl start docker

pip install --upgrade pip

pip3 install boto3

export AWS_DEFAULT_REGION="eu-west-1"

curl -L https://gist.githubusercontent.com/VJftw/a1cd61cdeae77bf8c27146379f04217c/raw/75ec49f09b070dc20d833ed72d1ce518376a2acb/docker_swarm.py -o docker_swarm.py

python3 docker_swarm.py
