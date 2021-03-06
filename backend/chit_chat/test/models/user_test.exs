defmodule ChitChat.UserTest do
  use ChitChat.ModelCase

  alias ChitChat.User

  @valid_attrs %{username: "alice", password: "abcdef123"}
  @invalid_attrs %{}

  test "changeset with valid attributes" do
    changeset = User.changeset(%User{}, @valid_attrs)

    {:ok, changeset} = User.validate_register(changeset)
    assert changeset.valid?
  end

  test "changeset with invalid attributes" do
    changeset = User.changeset(%User{}, @invalid_attrs)

    {:error, changeset} = User.validate_register(changeset)

    refute changeset.valid?
  end
end
