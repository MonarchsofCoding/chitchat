defmodule ChitChat.UserController do
  use ChitChat.Web, :controller

  alias ChitChat.User

  @doc """
  Lists all of the Users
  """
  def index(conn, _params) do
    users = Repo.all(User)
    render(conn, "index.json", users: users)
  end

  @doc """
  Creates a new User with the given parameters
  """
  def create(conn, %{"user" => user_params}) do
    changeset = User.changeset(%User{}, user_params)

    IO.inspect changeset

    if changeset.valid? do
      user = ChitChat.RegistrationDomain.register(changeset, Repo)
      conn
      |> put_status(:created)
      |> put_resp_header("location", user_path(conn, :show, user))
      |> render("show.json", user: user)
    else
      conn
      |> put_status(:unprocessable_entity)
      |> render(ChitChat.ChangesetView, "error.json", changeset: changeset)
    end

  end

end
