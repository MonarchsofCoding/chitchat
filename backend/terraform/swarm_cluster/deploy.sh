docker network create \
  --driver overlay \
  --subnet 10.0.9.0/24 \
  chitchat-network

docker service create \
  --replicas 1 \
  --name postgres-alpha \
  --network chitchat-network \
  --env POSTGRES_USER=test \
  --env POSTGRES_PASSWORD=test \
  --env POSTGRES_DB=test \
  postgres:latest

docker service create \
  --replicas 3 \
  --name chitchat-alpha \
  --network chitchat-network \
  --env SECRET_KEY_BASE=test \
  --env GUARDIAN_SECRET_KEY=test \
  --env DATABASE_HOSTNAME=postgres-alpha \
  --env DATABASE_PASSWORD=test \
  --env DATABASE_USERNAME=test \
  --env DATABASE_PASSWORD=test \
  --env DATABASE_PORT=5432 \
  --env PORT=80 \
  --env MIX_ENV=dev \
  --env NODE_COOKIE=test \
  monarchsofcoding/chitchat:release-4501ae444c386a6e2d25727434ae88b3cc407363 foreground
