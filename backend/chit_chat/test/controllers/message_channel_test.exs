defmodule ChitChat.MessageChannelTest do
  use ChitChat.ChannelCase
  use Phoenix.ConnTest

  alias ChitChat.MessageSocket

  def create_test_users(conn) do
    users = [
      %{username: "alice", password: "password1233"},
      %{username: "bob", password: "password1234"},
    ]

    Enum.each(users, fn(x) -> {
      conn
      |> Phoenix.ConnTest.recycle()
      |> Phoenix.ConnTest.post("/api/v1/users", x)
      |> Phoenix.ConnTest.json_response(201)
    } end
    )
  end

  describe "user:channel" do

    test "Authenticated user can connect to their channel" do
      conn = Phoenix.ConnTest.build_conn()

      create_test_users(conn)
      auth_response = conn
      |> Phoenix.ConnTest.recycle()
      |> Phoenix.ConnTest.post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> Phoenix.ConnTest.json_response(200)

      auth_token = auth_response["data"]["authToken"]

      {:ok, socket} = Phoenix.ChannelTest.connect(MessageSocket, %{
        "authToken" => auth_token
      })

      {:ok, message, _} = subscribe_and_join(socket, "user:alice", %{
        "authToken" => auth_token
      })

      assert message == %{message: "Joined user:alice"}
    end
    test "Authenticated user cannot connect to someone else's channel" do
      conn = Phoenix.ConnTest.build_conn()

      create_test_users(conn)
      auth_response = conn
      |> Phoenix.ConnTest.recycle()
      |> Phoenix.ConnTest.post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> Phoenix.ConnTest.json_response(200)

      auth_token = auth_response["data"]["authToken"]

      {:ok, socket} = Phoenix.ChannelTest.connect(MessageSocket, %{
        "authToken" => auth_token
      })

      assert {:error, %CaseClauseError{term: {:error, :function_clause}}} == subscribe_and_join(socket, "user:bob", %{
        "authToken" => auth_response
      })
    end

    test "Unauthenticated user is not allowed to connect to socket" do
      assert :error == Phoenix.ChannelTest.connect(MessageSocket, %{})
      assert :error == Phoenix.ChannelTest.connect(MessageSocket, %{"access_token" => "aaaaa"})
    end

    test "Unauthenticated user is not allowed to connect to channel" do
      conn = Phoenix.ConnTest.build_conn()

      create_test_users(conn)
      auth_response = conn
      |> Phoenix.ConnTest.recycle()
      |> Phoenix.ConnTest.post("/api/v1/auth", %{username: "alice", password: "password1233"})
      |> Phoenix.ConnTest.json_response(200)

      auth_token = auth_response["data"]["authToken"]

      {:ok, socket} = Phoenix.ChannelTest.connect(MessageSocket, %{
        "authToken" => auth_token
      })

      assert {:error, :authentication_required} == subscribe_and_join(socket, "user:alice", %{})
    end
  end
end
