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

  def changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:username, :password])
  end

  def validate_login_or_register_changeset(changeset) do
    changeset = changeset
    |> validate_required([:username, :password])
    |> validate_length(:password, min: 8)
    |> validate_length(:username, min: 3)

    if changeset.valid? do
      {:ok, changeset}
    else
      {:error, changeset}
    end

  end

  def validate_search_changeset(changeset) do
    changeset = changeset
    |> validate_required([:username])
    |> validate_length(:username, min: 3)

    if changeset.valid? do
      {:ok, changeset}
    else
      {:error, changeset}
    end

  end

  def search_all(username, user) do
    ChitChat.UserRepository.search(username, user)
  end

  @spec find_and_check_password(Ecto.Changeset) :: User
  def find_and_check_password(changeset) do

    with {:ok, user} <- ChitChat.UserRepository.find_by_username(changeset.params["username"]),
        {:ok, user} <- confirm_password(user, changeset)
    do
      {:ok, user}
    else
      {:error, changeset} ->
        changeset = changeset
        |> change
        |> add_error(:username, "invalid credentials")
        |> add_error(:password, "invalid credentials")

        {:error, changeset}
    end
  end

  @doc """

  """
  @spec register(Ecto.Changeset) :: struct
  def register(changeset) do
    changeset
    |> unique_constraint(:username)
    |> put_change(:hashed_password, Bcrypt.hashpwsalt(changeset.params["password"]))
    |> ChitChat.UserRepository.create()
  end

  def confirm_password(user, changeset) do
    case Bcrypt.checkpw(changeset.params["password"], user.hashed_password) do
      true ->
        {:ok, user}
      false ->
        {:error, changeset}
    end
  end

end
