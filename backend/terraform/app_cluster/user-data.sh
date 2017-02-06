#!/bin/bash
echo ""
echo "Joining ECS Cluster: ${ecs_cluster_name}!"
echo ""
echo ECS_CLUSTER=${ecs_cluster_name} >> /etc/ecs/ecs.config

echo ""
echo "Downloading ECS DNS scripts"
echo ""
curl -L https://raw.githubusercontent.com/awslabs/service-discovery-ecs-dns/1.0/ecssd_agent.conf -o /etc/init/ecssd_agent.conf
chmod 0644 /etc/init/ecssd_agent.conf
chown root:root /etc/init/ecssd_agent.conf

curl -L https://github.com/awslabs/service-discovery-ecs-dns/releases/download/1.2/ecssd_agent -o /usr/local/bin/ecssd_agent
chmod 0755 /usr/local/bin/ecssd_agent
chown root:root /usr/local/bin/ecssd_agent

echo ""
echo "Launching ECS DNS agent"
echo ""
start ecssd_agent
