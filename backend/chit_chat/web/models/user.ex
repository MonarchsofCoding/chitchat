defmodule ChitChat.User do
  @moduledoc """
  provides user entity.
  """

  use ChitChat.Web, :model

  schema "users" do
    field :username, :string
    field :password, :string, virtual: true

    field :hashed_password, :string

    timestamps()
  end

  @doc """
  Builds a changeset based on the struct and params.
  """
  @spec changeset(struct, {}) :: struct
  def changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:username, :password])
    |> validate_required([:username, :password])
    |> validate_length(:password, min: 8)
    |> validate_length(:username, min: 3)
    |> unique_constraint(:username)
  end
end
