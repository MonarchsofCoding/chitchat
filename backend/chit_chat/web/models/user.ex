defmodule ChitChat.User do
  @moduledoc """
  provides user entity.
  """

  use ChitChat.Web, :model

  alias Comeonin.Bcrypt
  alias ChitChat.UserRepository

  schema "users" do
    field :username, :string
    field :password, :string, virtual: true

    field :hashed_password, :string
    field :public_key, :string, default: ""
    field :online, :boolean

    timestamps()
  end

  @spec changeset(struct, {}) :: Ecto.Changeset
  def changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:username, :password, :public_key])
  end

  @spec validate_register(Ecto.Changeset) :: {}
  def validate_register(changeset) do
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

  @spec validate_login(Ecto.Changeset) :: {}
  def validate_login(changeset) do
    changeset = changeset
    |> validate_required([:username, :password, :public_key])

    if changeset.valid? do
      {:ok, changeset}
    else
      {:error, changeset}
    end
  end

  @spec validate_search(Ecto.Changeset) :: {}
  def validate_search(changeset) do
    changeset = changeset
    |> validate_required([:username])
    |> validate_length(:username, min: 3)

    if changeset.valid? do
      {:ok, changeset}
    else
      {:error, changeset}
    end

  end

  @spec get_by_username(String) :: User
  def get_by_username(username) do
    UserRepository.find_by_username(username)
  end

  @spec search_all(String, User) :: {}
  def search_all(username, user) do
    UserRepository.search(username, user)
  end

  @spec find_and_check_password(Ecto.Changeset) :: User
  def find_and_check_password(changeset) do
    with {:ok, user} <- UserRepository.find_by_username(
                          changeset.params["username"]
                        ),
        {:ok, user} <- confirm_password(user, changeset),
        changeset <- changeset(user, changeset.params)
    do
      UserRepository.save(changeset)
      {:ok, user}
    else
      _ ->
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
    |> put_change(:hashed_password, Bcrypt.hashpwsalt(
                          changeset.params["password"]))
    |> put_change(:online, false)
    |> UserRepository.create()
  end

  @spec confirm_password(User, Ecto.Changeset) :: {}
  def confirm_password(user, changeset) do
    case Bcrypt.checkpw(changeset.params["password"],
                        user.hashed_password) do
      true ->
        {:ok, user}
      false ->
        {:error, changeset}
    end
  end

end
