#!/bin/bash

set -e



if [ -z ${ECS_DNS_POSTGRES+x} ]; then
  echo "Not on AWS ECS."
  export VM_NAME=chitchat
  echo "Setting LOCAL_IP"
  export VM_IP=$(hostname -i)
  echo "Set LOCAL_IP as ${LOCAL_IP}"
else
  echo "On AWS ECS"
  /opt/app/bin/chit_chat command Elixir.ChitChat.ReleaseTasks aws_ecs_dns

  source db_env
  source cluster_env
fi

echo "Waiting for database to become available"
/bin/wait-for-it.sh -t 120 ${DATABASE_HOSTNAME}:${DATABASE_PORT}

env

/opt/app/bin/chit_chat $@
