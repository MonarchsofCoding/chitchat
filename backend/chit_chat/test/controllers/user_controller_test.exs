defmodule ChitChat.UserControllerTest do
  use ChitChat.ConnCase, async: true

  alias ChitChat.User

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  test "lists all entries on index" do

    users = [
      User.changeset(%User{}, %{username: "alice", password: "password1234"}),
      User.changeset(%User{}, %{username: "bob", password: "password123"})
    ]

    Enum.each(users, &Repo.insert!(&1))

    response = build_conn()
    |> get(user_path(build_conn(), :index))
    |> json_response(200)

    expected = %{
      "data" => [
        %{"username" => "alice"},
        %{"username" => "bob"}
      ]
    }

    assert response == expected

  end

  test "creates and renders User resource when data is valid", %{conn: conn} do
    conn = post conn, user_path(conn, :create), user: %{username: "bob", password: "password123"}

    assert json_response(conn, 201)["data"]["username"]
    assert Repo.get_by(User, %{username: "bob"})
  end

  test "does not create User resource and renders errors when data is invalid", %{conn: conn} do
    conn = post conn, user_path(conn, :create), user: %{}

    assert json_response(conn, 422)["errors"] != %{}
  end

  test "does not create duplicate User resource and renders errors when username is already taken", %{conn: conn} do

    conn = post conn, user_path(conn, :create), user: %{
      username: "bob",
      password: "password123"
    }

    conn = post conn, user_path(conn, :create), user: %{
      username: "bob",
      password: "password1234"
    }

    assert json_response(conn, 422)["errors"] != %{}
  end

end
