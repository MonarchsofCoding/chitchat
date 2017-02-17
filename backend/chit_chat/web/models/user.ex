defmodule ChitChat.User do
  @moduledoc """
  provides user entity.
  """

  use ChitChat.Web, :model

  alias Comeonin.Bcrypt

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
  end

  @doc """

  """
  @spec register(Repo, Ecto.Changeset) :: struct
  def register(repo, changeset) do
    if changeset.valid? do
      changeset
        |> unique_constraint(:username)
        |> put_change(:hashed_password, Bcrypt.hashpwsalt(changeset.params["password"]))
        |> repo.insert()
    else
      {:error, changeset}
    end

  end

  @doc """

  """
  @spec find_and_confirm_password(Repo, Ecto.Changeset) :: struct
  def find_and_confirm_password(repo, changeset) do
    user = repo.get_by(ChitChat.User, username: changeset.params["username"])
    if user do
      if Bcrypt.checkpw(changeset.params["password"], user.hashed_password) do
        {:ok, user}
      else
        {:error, changeset}
      end
    else
      {:error, changeset}
    end
  end
end
