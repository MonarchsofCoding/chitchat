# This file is responsible for configuring your application
# and its dependencies with the aid of the Mix.Config module.
#
# This configuration file is loaded before any dependency and
# is restricted to this project.
use Mix.Config

# General application configuration
config :chit_chat,
  ecto_repos: [ChitChat.Repo]

# Configures the endpoint
config :chit_chat, ChitChat.Endpoint,
  url: [host: "localhost"],
  secret_key_base: "y/WP7ajWvp0toKfMrOXNbtdN2JmQsLWQJL+KxXTaZDgvwsfU6zecM6k0T3DlQiC3",
  render_errors: [view: ChitChat.ErrorView, accepts: ~w(html json)],
  pubsub: [name: ChitChat.PubSub,
           adapter: Phoenix.PubSub.PG2]

# Configures Elixir's Logger
config :logger, :console,
  format: "$time $metadata[$level] $message\n",
  metadata: [:request_id]

config :libcluster,
  topologies: [
    gossip_example: [
      strategy: Elixir.Cluster.Strategy.Gossip,
    ]
  ]

# Configures Guardian JWT auth system
config :guardian, Guardian,
  allowed_algos: ["HS512"],
  verify_module: Guardian.JWT,
  issuer: "ChitChat",
  ttl: { 30, :days },
  allowed_drift: 2000,
  verify_issuer: true,
  secret_key: "cQCWxKpcuixeB4ZAxCs04nrBGdKeJiHcmmCHbZPI6esGcLcfZVz1qw2796p3gWGA",
  serializer: ChitChat.GuardianSerializer

# Import environment specific config. This must remain at the bottom
# of this file so it overrides the configuration defined above.
import_config "#{Mix.env}.exs"
