defmodule ChitChat.ErrorView do
  use ChitChat.Web, :view

  @spec render(String, {}) :: String
  def render("404.html", _assigns) do
    "Page not found"
  end

  @spec render(String, {}) :: String
  def render("500.html", _assigns) do
    "Internal server error"
  end

  # In case no render clause matches or no
  # template is found, let's render it as 500
  @spec template_not_found(String, {}) :: {}
  def template_not_found(_template, assigns) do
    render "500.html", assigns
  end
end
