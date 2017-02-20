defmodule ChitChat.UserController do
  use ChitChat.Web, :controller
  use Guardian.Phoenix.Controller

  alias ChitChat.User
  alias ChitChat.ChangesetView
  alias ChitChat.AuthView
  alias ChitChat.UserRepository

  @doc """
  Lists all of the Users filtered by User.username
  """
  @spec index(Conn, any, any, any) :: nil
  def index(conn, user_params, user, _claims) do

    case user != nil do
      true ->
        user_search = User.search_changeset(%User{}, user_params)

        case user_search.valid? do
          true ->
            users = UserRepository.search(user_search.params["username"], user)
            conn
            |> put_status(200)
            |> render("index.json", users: users)
          false ->
            conn
            |> put_status(:bad_request)
            |> render(ChangesetView, "error.json", changeset: user_search)
        end
      false ->
        conn
        |> put_status(:unauthorized)
        |> render(AuthView, "unauthorized.json", %{})
    end

  end

  @doc """
  Creates a new User with the given parameters
  """
  @spec create(Conn, {}, {}, {}) :: nil
  def create(conn, user_params, _user, _claims) do
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
        |> render(ChangesetView, "error.json", changeset: changeset)
    end

  end

end
