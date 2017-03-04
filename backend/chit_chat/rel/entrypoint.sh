#!/bin/bash

set -e

echo "Setting LOCAL_IP"
export LOCAL_IP=$(hostname -i)
echo "Set LOCAL_IP as ${LOCAL_IP}"

if [ -z ${ECS_DNS_POSTGRES+x} ]; then
  echo "Not on AWS ECS."
else
  echo "On AWS ECS"
  /opt/app/bin/chit_chat command Elixir.ChitChat.ReleaseTasks aws_ecs_dns

  source db_env
fi

echo "Waiting for database to become available"
/bin/wait-for-it.sh -t 120 ${DATABASE_HOSTNAME}:${DATABASE_PORT}

/opt/app/bin/chit_chat $@
