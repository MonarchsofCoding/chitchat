defmodule ChitChat.ReleaseTasks do
  @moduledoc """
  provides init tasks for releases.
  """

  alias Ecto.Migrator

  @start_apps [
    :postgrex,
    :ecto
  ]

  @repos [
    ChitChat.Repo
  ]

  def seed do
    # Load the code for ChitChat, but don't start it
    IO.write "> Loading Chit Chat..."
    :ok = Application.load(:chit_chat)
    IO.puts " Done."

    # Start apps necessary for executing migrations
    IO.write "> Starting dependencies..."
    Enum.each(@start_apps, &Application.ensure_all_started/1)
    IO.puts " Done."

    # Start the Repo(s) for myapp
    IO.write "> Starting repositories..."
    Enum.each(@repos, &apply(&1, :start_link, []))
    IO.puts " Done."

    # Run migrations
    Enum.each(@repos, &run_migrations_for/1)

    # Run the seed script if it exists
    seed_script = Path.join([priv_dir(), "repo", "seeds.exs"])
    if File.exists?(seed_script) do
      IO.write "> Running seed script..."
      Code.eval_file(seed_script)
      IO.puts " Done."
    end

    :init.stop()
  end

  def priv_dir, do: Application.app_dir(:chit_chat, "priv")

  defp run_migrations_for(app) do
    IO.puts "> Running migrations for #{app}..."
    Migrator.run(app, migrations_path(), :up, all: true)
  end

  defp migrations_path, do: Path.join([priv_dir(), "repo", "migrations"])

end
