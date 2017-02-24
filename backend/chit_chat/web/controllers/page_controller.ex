defmodule ChitChat.PageController do
  @moduledoc """
  provides the page functions.
  """
  use ChitChat.Web, :controller

  @spec index(Conn, {}) :: nil
  def index(conn, _params) do
    render conn, "index.html"
  end
end
