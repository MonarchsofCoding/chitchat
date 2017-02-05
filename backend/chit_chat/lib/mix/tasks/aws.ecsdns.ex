defmodule Mix.Tasks.Aws.EcsDns do
  use Mix.Task

  @shortdoc "Updates environment variables from services on ECS using DNS service discovery"

  @moduledoc """
    This is where we would put any long form documentation or doctests.
  """

  @spec run(any) :: nil
  def run(_args) do
    Mix.shell.info "Checking for services on AWS ECS using DNS service discovery..."
      if System.get_env("ECS_DNS_POSTGRES") do
        postgres_dns_address = to_charlist System.get_env("ECS_DNS_POSTGRES")
        Mix.shell.info "Looking for: #{postgres_dns_address}"

        :timer.sleep(2000)

        res = :inet_res.lookup(postgres_dns_address, :in, :srv)

        if length(res) > 0 do
          dns_data = List.first(res)
          db_port = elem(dns_data, 2)
          db_hostname = elem(dns_data, 3)

          Mix.shell.info "Found #{postgres_dns_address} at #{db_hostname}:#{db_port} "

          {:ok, env_file} = File.open "config/env", [:write]
          IO.binwrite env_file, "export DATABASE_HOSTNAME=#{to_string(db_hostname)}\n"
          IO.binwrite env_file, "export DATABASE_PORT=#{to_string(db_port)}\n"
        end
      end
  end

  # We can define other functions as needed here.
end
