defmodule ChitChat.AuthController do
  use ChitChat.Web, :controller

  alias ChitChat.User
  alias Guardian.Plug

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

    with {:ok, changeset} <- User.changeset(%User{}, user_params),
        {:ok, changeset} <- User.validate_login_changeset(changeset),
        {:ok, user, jwt, exp} <- User.authenticate(changeset)
    do
      conn
      |> put_status(:ok)
      |> render("auth.json", user: user, jwt: jwt, exp: exp)
    else
      {:error, changeset} ->
        conn
        |> put_status(:unauthorized)
        |> render(ChangesetView, "error.json", changeset: changeset)
    end

    # changeset = User.register_changeset(%User{}, user_params)
    #
    # case User.find_and_confirm_password(Repo, changeset) do
    #   {:ok, user} ->
    #     new_conn = Plug.api_sign_in(conn, user)
    #     jwt = Plug.current_token(new_conn)
    #     {:ok, claims} = Plug.claims(new_conn)
    #     exp = Map.get(claims, "exp")
    #     new_conn
    #     |> render("auth.json", user: user, jwt: jwt, exp: exp)
    #   {:error, changeset} ->
    #     conn
    #     |> put_status(:unauthorized)
    #     |> render(ChitChat.ChangesetView, "error.json", changeset: changeset)
    # end
  end

end
