#!/bin/bash

echo ""
echo "Updating ECS"
echo ""
sudo yum update -y ecs-init
sudo service docker restart
sudo start ecs

echo ""
echo "Joining ECS Cluster: ${ecs_cluster_name}!"
echo ""
echo ECS_CLUSTER=${ecs_cluster_name} >> /etc/ecs/ecs.config

echo ""
echo "Upgrading Weave Scope"
echo ""
sudo curl -L git.io/scope -o /usr/local/bin/scope
sudo chmod a+x /usr/local/bin/scope
sudo stop scope
sudo start scope

echo ""
echo "Upgrading Weave Net"
echo ""
sudo curl -L git.io/weave -o /usr/local/bin/weave
sudo chmod a+x /usr/local/bin/weave
sudo stop weave
sudo start weave
