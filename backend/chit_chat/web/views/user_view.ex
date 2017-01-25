defmodule ChitChat.UserView do
  use ChitChat.Web, :view

  @lint false
  def render("index.json", %{users: users}) do
    %{data: render_many(users, ChitChat.UserView, "user.json")}
  end

  @lint false
  def render("show.json", %{user: user}) do
    %{data: render_one(user, ChitChat.UserView, "user.json")}
  end

  @lint false
  def render("user.json", %{user: user}) do
    %{
      username: user.username,
    }
  end
end
