defmodule ChitChat do
  @moduledoc """
  Provides ChitChat application.
  """

  alias ChitChat.Endpoint

  use Application

  # See http://elixir-lang.org/docs/stable/elixir/Application.html
  # for more information on OTP Applications
  @spec start(:normal, {}) :: nil
  def start(_type, _args) do
    import Supervisor.Spec

    # Define workers and child supervisors to be supervised
    children = [
      # Start the Ecto repository
      supervisor(ChitChat.Repo, []),
      # Start the endpoint when the application starts
      supervisor(ChitChat.Endpoint, []),
      # Start your own worker by calling:
      #   ChitChat.Worker.start_link(arg1, arg2, arg3)
      # worker(ChitChat.Worker, [arg1, arg2, arg3]),
      supervisor(ChitChat.Presence, []),
    ]

    # See http://elixir-lang.org/docs/stable/elixir/Supervisor.html
    # for other strategies and supported options
    opts = [strategy: :one_for_one, name: ChitChat.Supervisor]
    Supervisor.start_link(children, opts)
  end

  # Tell Phoenix to update the endpoint configuration
  # whenever the application is updated.
  @spec config_change({}, {}, {}) :: :ok
  def config_change(changed, _new, removed) do
    Endpoint.config_change(changed, removed)
    :ok
  end
end
