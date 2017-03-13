defmodule ChitChat.AuthController do
  @moduledoc """
  provides the authentication functions.
  """
  use ChitChat.Web, :controller

  alias ChitChat.User
  alias Guardian.Plug
  alias ChitChat.ChangesetView
  alias ChitChat.Endpoint
  
  @spec authenticate(ChitChat.User) :: {}
  def authenticate(user) do
    if user != nil do
      {:ok, user}
    else
      {:error, :unauthorized}
    end
  end

  @spec create(Conn, {}) :: nil
  def create(conn, user_params) do

    with changeset <- User.changeset(%User{}, user_params),
        {:ok, changeset} <- User.validate_login(changeset),
        {:ok, user} <- User.find_and_check_password(changeset)
    do
      new_conn = Plug.api_sign_in(conn, user)
      jwt = Plug.current_token(new_conn)
      {:ok, claims} = Plug.claims(new_conn)
      exp = Map.get(claims, "exp")

      Endpoint.broadcast! "user:#{user.username}", "user:logout", %{}

      conn
      |> put_status(:ok)
      |> render("auth.json", user: user, jwt: jwt, exp: exp)
    else
      {:error, changeset} ->
        conn
        |> put_status(:unauthorized)
        |> render(ChangesetView, "error.json", changeset: changeset)
    end
  end

end
