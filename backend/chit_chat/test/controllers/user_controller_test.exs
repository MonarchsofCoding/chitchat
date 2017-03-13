defmodule ChitChat.UserControllerTest do
  use ChitChat.ConnCase, async: true

  alias ChitChat.User

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  def create_test_users(conn) do
    users = [
      %{username: "alice", password: "password1233"},
      %{username: "bob", password: "password1234"},
      %{username: "bobob", password: "password1235"}
    ]

    Enum.each(users, fn(x) -> {
      conn
      |> recycle()
      |> post("/api/v1/users", x)
      |> json_response(201)
    } end
    )
  end

  describe "index" do
    test "returns filtered list of users when request is authenticated and query param is valid and users have a public key", %{conn: conn} do

      create_test_users(conn)

      # Give Bob a public key
      conn
      |> recycle()
      |> post("/api/v1/auth", %{
        username: "bob",
        password: "password1234",
        public_key: "bob public key"})
      |> json_response(200)

      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{
        username: "alice",
        password: "password1233",
        public_key: "alice public key"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      search_response = conn
      |> recycle()
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> get("/api/v1/users?username=bob")
      |> json_response(200)

      assert search_response == %{
        "data" => [
          %{"username" => "bob",
            "public_key" => "bob public key"
          },
        ]
      }
    end

    test "returns Unauthorized when the request is not authenticated", %{conn: conn} do
      conn
        |> get("/api/v1/users?username=bob")
        |> json_response(401)
    end

    test "Removes authenticated user from the response", %{conn: conn} do
      create_test_users(conn)
      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{
        username: "bob",
        password: "password1234",
        public_key: "bob public key"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      search_response = conn
      |> recycle()
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> get("/api/v1/users?username=bob")
      |> json_response(200)

      assert search_response == %{
        "data" => []
      }
    end

    test "returns 400 when username parameter is invalid (too short)", %{conn: conn} do

      conn
      |> post("/api/v1/users", %{username: "bob", password: "password123"})
      |> json_response(201)

      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{
        username: "bob",
        password: "password123",
        public_key: "bob public key"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      conn
        |> recycle()
        |> put_req_header("authorization", "Bearer #{auth_token}")
        |> get("/api/v1/users?username=bo")
        |> json_response(400)
    end
  end

  describe "create" do
    test "creates and renders User resource when data is valid", %{conn: conn} do
      response = conn
      |> post("/api/v1/users", %{username: "bob", password: "password123"})
      |> json_response(201)

      assert response["data"]["username"] == "bob"
      assert Repo.get_by(User, %{username: "bob"})
    end

    test "does not create User resource and renders errors when data is invalid", %{conn: conn} do
      conn = conn
      |> post("/api/v1/users", %{username: "bo", password: "aa"})

      assert json_response(conn, 422)["errors"] != %{}
    end

    test "does not create duplicate User resource and renders errors when username is already taken", %{conn: conn} do
      conn
      |> post("/api/v1/users", %{username: "bob", password: "password123"})
      |> json_response(201)

      conn = conn
      |> recycle()
      |> put_req_header("accept", "application/json")
      |> post("/api/v1/users", %{username: "bob", password: "password123"})

      assert json_response(conn, 422)["errors"] != %{}

    end
  end

end
