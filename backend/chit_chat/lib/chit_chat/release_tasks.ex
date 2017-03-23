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

  @spec aws_ecs_dns() :: {}
  def aws_ecs_dns do
    IO.puts "Checking for services on AWS ECS using DNS service discovery..."
    if System.get_env("ECS_DNS_POSTGRES") do
      postgres_dns_address = to_charlist System.get_env("ECS_DNS_POSTGRES")
      IO.puts "Looking for: #{postgres_dns_address}"

      :timer.sleep(2000)

      res = :inet_res.lookup(postgres_dns_address, :in, :srv)

      if length(res) > 0 do
        dns_data = List.first(res)
        db_port = elem(dns_data, 2)
        db_hostname = elem(dns_data, 3)

        IO.puts "Found #{postgres_dns_address} at #{db_hostname}:#{db_port} "

        {:ok, env_file} = File.open "db_env", [:write]
        IO.binwrite env_file, "export DATABASE_HOSTNAME=#{to_string(db_hostname)}\n"
        IO.binwrite env_file, "export DATABASE_PORT=#{to_string(db_port)}\n"
      end
    end

    :init.stop()
  end

  @spec aws_cluster() :: {}
  def aws_cluster do
    {:ok, env_file} = File.open "cluster_env", [:write]
    rand_bytes = :crypto.strong_rand_bytes(16)
    encoded_bytes = Base.url_encode64(rand_bytes)
    container_name = binary_part(encoded_bytes, 0, 16)

    IO.binwrite env_file, "export VM_NAME=#{to_string(container_name)}\n"
    # http://169.254.169.254/latest/meta-data/local-ipv4
    Application.ensure_all_started(:httpotion)
    response = HTTPotion.get "http://169.254.169.254/latest/meta-data/local-ipv4"
    IO.puts response.status_code
    IO.puts String.trim(response.body)
    IO.puts "\n> Generated unique node name #{container_name}@#{response.body}\n"
    IO.binwrite env_file, "export VM_IP=#{to_string(String.trim(response.body))}\n"
    :init.stop()
  end

end
