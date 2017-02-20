defmodule ChitChat.UserControllerTest do
  use ChitChat.ConnCase, async: true

  alias ChitChat.User

  test "returns filtered list of users when request is authenticated and query param is valid" do
    users = [
      %{username: "alice", password: "password1233"},
      %{username: "bob", password: "password1234"},
      %{username: "bobob", password: "password1235"}
    ]

    # Add users
    Enum.each(users, fn(x) -> {
        build_conn()
        |> put_req_header("accept", "application/json")
        |> post("/api/v1/users", x)
        |> json_response(201)
      } end
    )

    # Authenticate
    auth_response = build_conn()
      |> put_req_header("accept", "application/json")
      |> post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> json_response(200)

    auth_token = auth_response["data"]["authToken"]

    search_response = build_conn()
      |> put_req_header("accept", "application/json")
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> get("/api/v1/users?username=bob")
      |> json_response(200)

    assert search_response == %{
      "data" => [
        %{"username" => "bob"},
        %{"username" => "bobob"}
      ]
    }


    # conn = get conn, user_path(conn, :index), %{username: "ali"}

    # assert json_response(conn, 200) == %{"data" => [%{"username" => "bob"}, %{"username" => "bobob"}]}

  end

  test "creates and renders User resource when data is valid", %{conn: conn} do
    conn = post conn, user_path(conn, :create), %{username: "bob", password: "password123"}

    assert json_response(conn, 201)["data"]["username"]
    assert Repo.get_by(User, %{username: "bob"})
  end

  test "does not create User resource and renders errors when data is invalid", %{conn: conn} do
    conn = post conn, user_path(conn, :create), %{}

    assert json_response(conn, 422)["errors"] != %{}
  end

  test "does not create duplicate User resource and renders errors when username is already taken", %{conn: conn} do

    conn = post conn, user_path(conn, :create), %{
      username: "bob",
      password: "password123"
    }

    conn = post conn, user_path(conn, :create), %{
      username: "bob",
      password: "password1234"
    }

    assert json_response(conn, 422)["errors"] != %{}
  end

end
