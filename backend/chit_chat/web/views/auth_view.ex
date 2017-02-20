defmodule ChitChat.AuthView do
  use ChitChat.Web, :view

  @spec render(String, {:user, :jwt, :exp}) :: {}
  def render("auth.json", %{user: user, jwt: jwt, exp: exp}) do
    %{
      data: %{
        username: user.username,
        authToken: jwt,
        exp: exp
      }
    }
  end

  @spec render(String, {}) :: {}
  def render("unauthorized.json", %{}) do
    %{
      error: "Unauthorized"
    }
  end

end
