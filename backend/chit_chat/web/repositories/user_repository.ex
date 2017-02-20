defmodule ChitChat.UserRepository do
  @moduledoc """
  provides repository functions for Users.
  """
  import Ecto.Query, only: [from: 2]

  alias ChitChat.User
  alias ChitChat.Repo

  @spec search(String, User) :: []
  def search(username_fragment, excluded_user) do
    Repo.all(from u in User,
      where: ilike(u.username, ^"%#{username_fragment}%") and
      u.username != ^excluded_user.username
    )
  end

end
