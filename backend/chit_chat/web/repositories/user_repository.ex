defmodule ChitChat.UserRepository do
  @moduledoc """
  provides repository functions for Users.
  """
  import Ecto.Query, only: [from: 2]

  alias ChitChat.User
  alias ChitChat.Repo

  @spec search(String, User) :: []
  def search(username_fragment, excluded_user) do
    users = Repo.all(from u in User,
      where: ilike(u.username, ^"%#{username_fragment}%") and
      u.username != ^excluded_user.username
    )

    {:ok, users}
  end

  @spec find_by_username(String) :: User
  def find_by_username(username) do
    user = Repo.get_by(User, username: username)
    if user != nil do
      {:ok, user}
    else
      {:error}
    end
  end

  @spec create(Ecto.Changeset) :: {}
  def create(changeset) do
    Repo.insert(changeset)
  end

  def save(changeset) do
    Repo.update(changeset)
  end

end
