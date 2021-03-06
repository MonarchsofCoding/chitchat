#!/bin/bash

set -e

if [ -z ${ECS_DNS_POSTGRES+x} ]; then
  export VM_NAME=chitchat
  echo "Setting VM_IP"
  export VM_IP=$(hostname -i)
  echo "Set VM_IP as ${LOCAL_IP}"
else
  echo "On AWS ECS"
  /opt/app/bin/chit_chat command Elixir.ChitChat.ReleaseTasks aws_ecs_dns
  source db_env

  /opt/app/bin/chit_chat command Elixir.ChitChat.ReleaseTasks aws_cluster
  source cluster_env
fi

echo "Waiting for database to become available"
/bin/wait-for-it.sh -t 120 ${DATABASE_HOSTNAME}:${DATABASE_PORT}

/opt/app/bin/chit_chat $@
