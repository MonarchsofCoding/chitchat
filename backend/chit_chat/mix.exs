defmodule ChitChat.Mixfile do
  use Mix.Project

  def project do
    [
      app: :chit_chat,
      version: "0.0.1",
      elixir: "~> 1.2",
      elixirc_paths: elixirc_paths(Mix.env),
      compilers: [:phoenix, :gettext] ++ Mix.compilers,
      build_embedded: Mix.env == :prod,
      start_permanent: Mix.env == :prod,
      aliases: aliases(),
      deps: deps(),
      test_coverage: [tool: ExCoveralls],
      preferred_cli_env: ["coveralls": :test, "coveralls.detail": :test, "coveralls.post": :test, "coveralls.html": :test],
    ]
  end

  # Configuration for the OTP application.
  #
  # Type `mix help compile.app` for more information.
  def application do
    [mod: {ChitChat, []},
      applications: [
        :phoenix,
        :phoenix_pubsub,
        :phoenix_html,
        :cowboy,
        :logger,
        :gettext,
        :phoenix_ecto,
        :postgrex,
        :comeonin,
        :jose,
        :guardian,
        :elixir_make,
        :libcluster,
        :distillery,
        :httpotion
      ]
    ]
  end

  # Specifies which paths to compile per environment.
  defp elixirc_paths(:test), do: ["lib", "web", "test/support"]
  defp elixirc_paths(_),     do: ["lib", "web"]

  # Specifies your project dependencies.
  #
  # Type `mix help deps` for examples and options.
  defp deps do
    [
      {:phoenix, "~> 1.2.1"},
      {:phoenix_pubsub, "~> 1.0"},
      {:phoenix_ecto, "~> 3.0"},
      {:postgrex, ">= 0.0.0"},
      {:phoenix_html, "~> 2.6"},
      {:phoenix_live_reload, "~> 1.0", only: :dev},
      {:gettext, "~> 0.11"},
      {:cowboy, "~> 1.0"},
      {:comeonin, "~> 3.0"},
      {:excoveralls, "~> 0.5", only: :test},
      {:distillery, "~> 1.0"},
      {:credo, "~> 0.5", only: [:dev, :test]},
      {:dogma, "~> 0.1", only: [:dev, :test]},
      {:guardian, "~> 0.14"},
      {:jose, "~> 1.8"},
      {:libcluster, "~> 2.0.3"},
      {:tesla, "~> 0.6.0"},
      {:httpotion, "~> 3.0.2"},
      # {:mix_docker, "~> 0.3.2"}
    ]
  end

  # Aliases are shortcuts or tasks specific to the current project.
  # For example, to create, migrate and run the seeds file at once:
  #
  #     $ mix ecto.setup
  #
  # See the documentation for `Mix` for more info on aliases.
  defp aliases do
    ["ecto.setup": ["ecto.create", "ecto.migrate", "run priv/repo/seeds.exs"],
     "ecto.reset": ["ecto.drop", "ecto.setup"],
     "test": ["ecto.create --quiet", "ecto.migrate", "test"]]
  end
end
