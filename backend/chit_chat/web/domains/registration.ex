defmodule ChitChat.RegistrationDomain do
  @moduledoc """
  Provides Registration functionality.
  """

  use ChitChat.Web, :model

  @spec register(struct, Repo) :: struct
  def register(changeset, repo) do
    if changeset.valid? do
      changeset
        |> put_change(:hashed_password, hash_password(changeset.params["password"]))
        |> repo.insert()
    else
      {:error, changeset}
    end

  end

  defp hash_password(plain_password) do
    Comeonin.Bcrypt.hashpwsalt(plain_password)
  end

end
