defmodule ChitChat.ReleaseTasks do

  @start_apps [
    :postgrex,
    :ecto
  ]

  @repos [
    ChitChat.Repo
  ]

  def seed do
    IO.puts "Loading chit chat.."
    # Load the code for myapp, but don't start it
    :ok = Application.load(:chit_chat)

    IO.puts "Starting dependencies.."
    # Start apps necessary for executing migrations
    Enum.each(@start_apps, &Application.ensure_all_started/1)

    # Start the Repo(s) for myapp
    IO.puts "Starting repos.."
    Enum.each(@repos, &apply(&1, :start_link, []))

    # Run migrations
    Enum.each(@repos, &run_migrations_for/1)

    # Run the seed script if it exists
    seed_script = Path.join([priv_dir(:chit_chat), "repo", "seeds.exs"])
    if File.exists?(seed_script) do
      IO.puts "Running seed script.."
      Code.eval_file(seed_script)
    end

    # Signal shutdown
    IO.puts "Success!"
    :init.stop()
  end

  def priv_dir(app), do: "#{:code.priv_dir(app)}"

  defp run_migrations_for(app) do
    IO.puts "Running migrations for #{app}"
    Ecto.Migrator.run(app, migrations_path(app), :up, all: true)
  end

  defp migrations_path(app), do: Path.join([priv_dir(app), "repo", "migrations"])
  defp seed_path(app), do: Path.join([priv_dir(app), "repo", "seeds.exs"])

end
