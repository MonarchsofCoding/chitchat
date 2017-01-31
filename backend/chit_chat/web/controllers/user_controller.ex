defmodule ChitChat.UserController do
  use ChitChat.Web, :controller

  alias ChitChat.User


  @doc """
  Lists all of the Users
  """
  @spec index(Conn, {}) :: nil
  def index(conn, _params) do
    users = Repo.all(User)
    render(conn, "index.json", users: users)
  end

  @doc """
  Creates a new User with the given parameters
  """
  @spec create(Conn, {}) :: nil
  def create(conn, user_params) do
    changeset = User.changeset(%User{}, user_params)

    case ChitChat.RegistrationDomain.register(changeset, Repo) do
      {:ok, user} ->
        conn
        |> put_status(:created)
        |> put_resp_header("location", user_path(conn, :show, user))
        |> render("show.json", user: user)
      {:error, changeset} ->
        conn
        |> put_status(:unprocessable_entity)
        |> render(ChitChat.ChangesetView, "error.json", changeset: changeset)
    end

  end

end
