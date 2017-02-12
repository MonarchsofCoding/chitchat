defmodule ChitChat.AuthControllerTest do
  use ChitChat.ConnCase, async: true

  alias ChitChat.User

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  test "creates and renders auth token when user data is valid", %{conn: conn} do
    # Create a User
    conn = post conn, user_path(conn, :create), %{
      username: "bob",
      password: "password123"
    }

    conn = post conn, auth_path(conn, :create), %{
      username: "bob",
      password: "password123"
    }

    assert json_response(conn, 200)["data"]["authToken"]
  end

  test "does not render auth token when user data is invalid", %{conn: conn} do
    # Create a User
    conn = post conn, user_path(conn, :create), %{
      username: "bob",
      password: "password123"
    }

    conn = post conn, auth_path(conn, :create), %{
      username: "bob",
      password: "password1243"
    }

    assert json_response(conn, 401)["errors"] == %{}
  end

end
