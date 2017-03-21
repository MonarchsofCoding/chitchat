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
      |> post("/api/v1/auth", %{
        username: "alice",
        password: "password1233",
        public_key: "A public key"
        })
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
      |> post("/api/v1/auth", %{
        username: "alice",
        password: "password1233",
        public_key: "A public key"})
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
      |> post("/api/v1/auth", %{
        username: "alice",
        password: "password1233",
        public_key: "A public key"})
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

end
