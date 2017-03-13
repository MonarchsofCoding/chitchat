defmodule ChitChat.UserController do
  @moduledoc """
  provides the user functions.
  """
  use ChitChat.Web, :controller
  use Guardian.Phoenix.Controller

  alias ChitChat.User
  alias ChitChat.ChangesetView
  alias ChitChat.ErrorView
  alias ChitChat.AuthController

  @doc """
  Lists all of the Users filtered by User.username
  """
  @spec index(Conn, any, any, any) :: nil
  def index(conn, user_params, user, _claims) do

    with {:ok, user} <- AuthController.authenticate(user),
        changeset <- User.changeset(%User{}, user_params),
        {:ok, changeset} <- User.validate_search(changeset),
        {:ok, users} <- User.search_all(changeset.params["username"], user)
    do
      conn
      |> put_status(200)
      |> render("index.json", users: users)
    else
      {:error, status} when is_atom(status) ->
        conn
        |> put_status(status)
        |> render(ErrorView, "status.json", status: status)
      {:error, changeset} ->
        conn
        |> put_status(:bad_request)
        |> render(ChangesetView, "error.json", changeset: changeset)
    end
  end

  @doc """
  Creates a new User with the given parameters
  """
  @spec create(Conn, {}, {}, {}) :: nil
  def create(conn, user_params, _user, _claims) do

    with changeset <- User.changeset(%User{}, user_params),
        {:ok, changeset} <- User.validate_register(changeset),
        {:ok, user} <- User.register(changeset)
    do
      conn
      |> put_status(:created)
      |> put_resp_header("location", user_path(conn, :show, user))
      |> render("show.json", user: user)
    else
      {:error, changeset} ->
        conn
        |> put_status(:unprocessable_entity)
        |> render(ChangesetView, "error.json", changeset: changeset)
    end
  end

end
