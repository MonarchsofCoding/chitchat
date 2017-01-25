defmodule ChitChat.RegistrationDomain do

  use ChitChat.Web, :model

  def register(changeset, repo) do
    changeset
      |> put_change(:hashed_password, hash_password(changeset.params["password"]))
      |> repo.insert!()
  end

  defp hash_password(plain_password) do
    Comeonin.Bcrypt.hashpwsalt(plain_password)
  end

end
