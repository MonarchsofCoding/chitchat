import boto3

# 1. Find cluster security group.
#  - Allow access via SSH to the security group.
# 2. Connect to Manager node via SSH.
#  - Run sudo docker service create ...
#  - Disconnect.
# 3. Remove access via SSH from the security group.



def __find_cluster_security_group(swarm_name):
  client = boto3.client('ec2')
  sec_groups = client.describe_security_groups(Filters=[
  {
    'Name': 'tag-key',
    'Values': [
      'swarm_name'
    ]
  },
  {
    'Name': 'tag-value',
    'Values': [
      swarm_name
    ]
  }
  ])['SecurityGroups']



  print(sec_groups)

if __name__ == "__main__":
  ## args
  security_group = __find_cluster_security_group(swarm_name)
