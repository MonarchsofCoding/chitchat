defmodule ChitChat.AuthControllerTest do
  use ChitChat.ConnCase, async: true

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  test "creates and renders auth token when user data is valid", %{conn: conn} do

    conn
    |> recycle()
    |> post("/api/v1/users", %{username: "bob", password: "password123"})
    |> json_response(201)

    auth_response = conn
    |> recycle()
    |> post("/api/v1/auth", %{username: "bob", password: "password123"})
    |> json_response(200)

    auth_token = auth_response["data"]["authToken"]

    assert auth_token != ""
  end

  test "does not render auth token when user data is invalid", %{conn: conn} do
    conn
    |> recycle()
    |> post("/api/v1/users", %{username: "bob", password: "password123"})
    |> json_response(201)

    conn
    |> recycle()
    |> post("/api/v1/auth", %{username: "bob", password: "password1234"})
    |> json_response(401)
  end

  test "does not render auth token when username does not exist", %{conn: conn} do
    conn
    |> recycle()
    |> post("/api/v1/auth", %{username: "bob", password: "password1234"})
    |> json_response(401)
  end

end
