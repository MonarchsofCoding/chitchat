use Mix.Config

# We don't run a server during test. If one is required,
# you can enable the server option below.
config :chit_chat, ChitChat.Endpoint,
  http: [port: 4001],
  server: false

# Print only warnings and errors during test
config :logger, level: :warn

# Configure your database
config :chit_chat, ChitChat.Repo,
  adapter: Ecto.Adapters.Postgres,
  username: "postgres",
  password: "postgres",
  database: "chit_chat_test",
  hostname: "postgres",
  pool: Ecto.Adapters.SQL.Sandbox

# Use less encryption rounds in test environment
config :comeonin, :bcrypt_log_rounds, 4
