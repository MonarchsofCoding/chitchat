defmodule ChitChat.MessageControllerTest do
  use ChitChat.ConnCase, async: true

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  def create_test_users(conn) do
    users = [
      %{username: "alice", password: "password1233"},
      %{username: "bob", password: "password1234"},
    ]

    Enum.each(users, fn(x) -> {
      conn
      |> recycle()
      |> post("/api/v1/users", x)
      |> json_response(201)
    } end
    )
  end

  describe "create" do
    test "returns unauthorized if the request is not authenticated", %{conn: conn} do
      create_test_users(conn)

      message_response = conn
      |> recycle()
      |> post("/api/v1/messages", %{recipient: "bob", message: "Hello, Bob!"})
      |> json_response(401)

      assert message_response == %{"reason" => "Unauthorized"}
    end
    test "returns unprocessable entity if the request contains invalid parameters", %{conn: conn} do
      create_test_users(conn)
      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      message_response = conn
      |> recycle()
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> post("/api/v1/messages", %{recipient: "", message: ""})
      |> json_response(422)

      assert message_response == %{
        "errors" => %{
          "recipient" => ["can't be blank"],
          "message" => ["can't be blank"]
        }
      }

    end
    test "returns unprocessable entity if the recipient cannot be found", %{conn: conn} do
      create_test_users(conn)
      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      message_response = conn
      |> recycle()
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> post("/api/v1/messages", %{recipient: "charlie", message: "Hello, Charlie!"})
      |> json_response(422)

      assert message_response == %{
        "errors" => %{
          "recipient" => ["does not exist"],
        }
      }
    end
    test "returns the message if given valid parameters", %{conn: conn} do
      create_test_users(conn)
      auth_response = conn
      |> recycle()
      |> post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> json_response(200)

      auth_token = auth_response["data"]["authToken"]

      message_response = conn
      |> recycle()
      |> put_req_header("authorization", "Bearer #{auth_token}")
      |> post("/api/v1/messages", %{recipient: "bob", message: "Hello, Bob!"})
      |> json_response(201)

      assert message_response == %{
        "data" => %{
          "recipient" => "bob",
          "sender" => "alice",
          "message" => "Hello, Bob!"
        }
      }
    end
  end

  #   test "returns filtered list of users when request is authenticated and query param is valid", %{conn: conn} do
  #
  #     create_test_users(conn)
  #     auth_response = conn
  #     |> recycle()
  #     |> post("/api/v1/auth", %{username: "alice", password: "password1233"})
  #     |> json_response(200)
  #
  #     auth_token = auth_response["data"]["authToken"]
  #
  #     search_response = conn
  #     |> recycle()
  #     |> put_req_header("authorization", "Bearer #{auth_token}")
  #     |> get("/api/v1/users?username=bob")
  #     |> json_response(200)
  #
  #     assert search_response == %{
  #       "data" => [
  #         %{"username" => "bob"},
  #         %{"username" => "bobob"}
  #       ]
  #     }
  #   end
  #
  #   test "returns Unauthorized when the request is not authenticated", %{conn: conn} do
  #     conn
  #       |> get("/api/v1/users?username=bob")
  #       |> json_response(401)
  #   end
  #
  #   test "Removes authenticated user from the response", %{conn: conn} do
  #     create_test_users(conn)
  #     auth_response = conn
  #     |> recycle()
  #     |> post("/api/v1/auth", %{username: "bob", password: "password1234"})
  #     |> json_response(200)
  #
  #     auth_token = auth_response["data"]["authToken"]
  #
  #     search_response = conn
  #     |> recycle()
  #     |> put_req_header("authorization", "Bearer #{auth_token}")
  #     |> get("/api/v1/users?username=bob")
  #     |> json_response(200)
  #
  #     assert search_response == %{
  #       "data" => [
  #         %{"username" => "bobob"}
  #       ]
  #     }
  #   end
  #
  #   test "returns 400 when username parameter is invalid (too short)", %{conn: conn} do
  #
  #     conn
  #     |> post("/api/v1/users", %{username: "bob", password: "password123"})
  #     |> json_response(201)
  #
  #     auth_response = conn
  #     |> recycle()
  #     |> post("/api/v1/auth", %{username: "bob", password: "password123"})
  #     |> json_response(200)
  #
  #     auth_token = auth_response["data"]["authToken"]
  #
  #     conn
  #       |> recycle()
  #       |> put_req_header("authorization", "Bearer #{auth_token}")
  #       |> get("/api/v1/users?username=bo")
  #       |> json_response(400)
  #   end
  # end
  #
  # describe "create" do
  #   test "creates and renders User resource when data is valid", %{conn: conn} do
  #     response = conn
  #     |> post("/api/v1/users", %{username: "bob", password: "password123"})
  #     |> json_response(201)
  #
  #     assert response["data"]["username"] == "bob"
  #     assert Repo.get_by(User, %{username: "bob"})
  #   end
  #
  #   test "does not create User resource and renders errors when data is invalid", %{conn: conn} do
  #     conn = conn
  #     |> post("/api/v1/users", %{username: "bo", password: "aa"})
  #
  #     assert json_response(conn, 422)["errors"] != %{}
  #   end
  #
  #   test "does not create duplicate User resource and renders errors when username is already taken", %{conn: conn} do
  #     conn
  #     |> post("/api/v1/users", %{username: "bob", password: "password123"})
  #     |> json_response(201)
  #
  #     conn = conn
  #     |> recycle()
  #     |> put_req_header("accept", "application/json")
  #     |> post("/api/v1/users", %{username: "bob", password: "password123"})
  #
  #     assert json_response(conn, 422)["errors"] != %{}
  #
  #   end
  # end

end
