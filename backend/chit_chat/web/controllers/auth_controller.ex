defmodule ChitChat.AuthController do
  use ChitChat.Web, :controller

  alias ChitChat.User
  alias Guardian.Plug

  @spec create(Conn, {}) :: nil
  def create(conn, user_params) do
    changeset = User.register_changeset(%User{}, user_params)

    case User.find_and_confirm_password(Repo, changeset) do
      {:ok, user} ->
        new_conn = Plug.api_sign_in(conn, user)
        jwt = Plug.current_token(new_conn)
        {:ok, claims} = Plug.claims(new_conn)
        exp = Map.get(claims, "exp")
        new_conn
        |> render("auth.json", user: user, jwt: jwt, exp: exp)
      {:error, changeset} ->
        conn
        |> put_status(:unauthorized)
        |> render(ChitChat.ChangesetView, "error.json", changeset: changeset)
    end
  end

end
