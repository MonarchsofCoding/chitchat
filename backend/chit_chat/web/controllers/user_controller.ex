defmodule ChitChat.UserController do
  use ChitChat.Web, :controller

  alias ChitChat.User

  @doc """
  Lists all of the Users filtered by User.username
  """
  @spec index(Conn, {}) :: nil
  def index(conn, user_params) do

    changeset = User.search_changeset(%User{}, user_params)

    case changeset.valid? do
      true ->
        username_query = changeset.params["username"]
        users = Repo.all(from u in User, where: ilike(u.username, ^"%#{username_query}%"))

        conn
        |> put_status(200)
        |> render("index.json", users: users)

      false ->
        conn
        |> put_status(:bad_request)
        |> render(ChitChat.ChangesetView, "error.json", changeset: changeset)
    end
  end

  @doc """
  Creates a new User with the given parameters
  """
  @spec create(Conn, {}) :: nil
  def create(conn, user_params) do
    changeset = User.register_changeset(%User{}, user_params)

    case User.register(Repo, changeset) do
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
