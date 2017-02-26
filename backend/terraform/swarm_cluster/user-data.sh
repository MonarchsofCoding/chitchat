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

docker run hello-world

pip install --upgrade pip

pip3 install boto3

export SWARM_NAME="test"
export AWS_DEFAULT_REGION="eu-west-1"

curl -L https://gist.githubusercontent.com/VJftw/a1cd61cdeae77bf8c27146379f04217c/raw/c8d0b06671c7f40ac829d4467bb9bae6bded7638/docker_swarm.py -o docker_swarm.py

python3 docker_swarm.py
