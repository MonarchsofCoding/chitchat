import boto3
import botocore
import urllib.request
import subprocess
import time
import os

#
# Uses DyanmoDB to aid in initialising a Docker Swarm using locking.
#

local_hostname = urllib.request.urlopen('http://169.254.169.254/latest/meta-data/local-hostname').read().decode("utf-8").strip()
local_ip = urllib.request.urlopen('http://169.254.169.254/latest/meta-data/local-ipv4').read().decode("utf-8").strip()
instance_id = urllib.request.urlopen('http://169.254.169.254/latest/meta-data/instance-id').read().decode("utf-8").strip()

lock_table_name = "docker_swarm_locks"
dynamodb = boto3.resource('dynamodb')
ec2 = boto3.resource('ec2')
instance = ec2.Instance(instance_id)

swarm_name = os.getenv("SWARM_NAME")
for tag in instance.tags:
  if tag['Key'] == "swarm":
    swarm_name = tag['Value'].strip()
    break

def main():

  try:
    print("Requesting creation of DynamoDB table: {0}".format(lock_table_name))
    lock_table = __create_lock_table(dynamodb, lock_table_name)
  except Exception:
    print("Using existing {0} DynamoDB table.".format(lock_table_name))
    lock_table = dynamodb.Table(lock_table_name)
    # lock_table = dynamodb.Table(lock_table_name)
    # lock_table.delete()
    # lock_table.wait_until_not_exists()
    # exit()

  print("Waiting for {0} DynamoDB table to exist.".format(lock_table_name))
  lock_table.wait_until_exists()

  try:
    __acquire_lock(lock_table, swarm_name, local_ip)
    # docker swarm init
    subprocess.run(["docker", "swarm", "init", "--advertise-addr", local_ip, "--listen-addr", "{0}:2377".format(local_ip)], check=True)

    p = subprocess.run(["docker", "swarm", "join-token", "--quiet", "worker"], stdout=subprocess.PIPE)
    worker_token = p.stdout.decode("utf-8").strip()

    p = subprocess.run(["docker", "swarm", "join-token", "--quiet", "manager"], stdout=subprocess.PIPE)
    manager_token = p.stdout.decode("utf-8").strip()

    __update_swarm_tokens(lock_table, swarm_name, local_ip, worker_token, manager_token)

    __tag_current_instance('manager')

    # Deploy Portainer on Manager
    subprocess.run(["docker", "service", "create",
    "--name", "portainer",
    "--publish", "9000:9000",
    "--constraint", "node.role == manager",
    "--mount", "type=bind,src=/var/run/docker.sock,dst=/var/run/docker.sock",
    "portainer/portainer",
    "-H", "unix:///var/run/docker.sock"
    ])


  except Exception:
    # TODO: automatic decision on 'worker' or 'manager' (need to track amount of other nodes in the ASG)
    swarm_info = __get_swarm_info(lock_table, swarm_name)

    p = subprocess.run(["docker", "swarm", "join", "--token", swarm_info['worker_token'], swarm_info['node_ip']])

    __tag_current_instance('worker')


def __tag_current_instance(swarm_role):
  print("Tagging self with swarm.role: {0}".format(swarm_role))

  instance.create_tags(
    DryRun=False,
    Tags=[
      {
          'Key': 'swarm.role',
          'Value': swarm_role
      },
    ]
  )


def __update_swarm_tokens(table, swarm_name, local_ip, worker_token, manager_token):
  table.put_item(
    Item={
      'swarm_name': swarm_name,
      'node_ip': local_ip,
      'worker_token': worker_token,
      'manager_token': manager_token
    }
  )

def __get_swarm_info(table, swarm_name):
  info = table.get_item(
    Key={
      'swarm_name': swarm_name
    }
  )
  if "worker_token" not in info['Item']:
    time.sleep(5)
    return __get_swarm_info(table, swarm_name)

  return info['Item']


def __release_lock(table, swarm_name):
  resp = table.delete_item(
    Key={
      'swarm_name': swarm_name
    }
  )
  print("Lock released for {0} swarm".format(swarm_name))

def __acquire_lock(table, swarm_name, local_ip):
  resp = table.put_item(
    Item={
      'swarm_name': swarm_name,
      'node_ip': local_ip
    },
    ConditionExpression=boto3.dynamodb.conditions.Attr('swarm_name').not_exists()
  )
  print("Lock acquired for {0} swarm".format(swarm_name))

def __create_lock_table(dynamodb, lock_table_name):
  lock_table = dynamodb.create_table(
    TableName=lock_table_name,
    KeySchema=[
      {
        'AttributeName': 'swarm_name',
        'KeyType': 'HASH'
      }
    ],
    AttributeDefinitions=[
      {
        'AttributeName': 'swarm_name',
        'AttributeType': 'S'
      }
    ],
    ProvisionedThroughput={
      'ReadCapacityUnits': 5,
      'WriteCapacityUnits': 5
    }
  )
  return lock_table

if __name__ == "__main__":
  main()
