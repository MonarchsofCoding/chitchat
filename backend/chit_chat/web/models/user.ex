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

  def validate_login_changeset(changeset) do
    changeset
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
    changeset
    |> validate_required([:username])
    |> validate_length(:username, min: 3)

    # if changeset.valid? do
    #   {:ok, changeset}
    # else
    #   {:error, changeset}
    # end
  end

  @spec authenticate(Ecto.Changeset, Plug.Conn) :: User
  def authenticate(changeset, conn) do
    with {:ok, user} <- ChitChat.UserRepository.find_by_username(changeset.params["username"]),
        {:ok, user} <- confirm_password(user, changeset)
    do
      new_conn = Plug.api_sign_in(conn, user)
      jwt = Plug.current_token(new_conn)
      {:ok, claims} = Plug.claims(new_conn)
      exp = Map.get(claims, "exp")

      {:ok, user, jwt, exp}
    else
      {:error, changeset}
    end
  end


  @doc """
  Builds a changeset based on the struct and params for registration
  """
  @spec register_changeset(struct, {}) :: struct
  def register_changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:username, :password])
    |> validate_required([:username, :password])
    |> validate_length(:password, min: 8)
    |> validate_length(:username, min: 3)
  end

  @doc """
  Builds a changeset based on the struct and params for searching
  """
  @spec search_changeset(struct, {}) :: struct
  def search_changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:username])
    |> validate_required([:username])
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

  def confirm_password(user, changeset) do
    case Bcrypt.checkpw(changeset.params["password"], user.hashed_password) do
      true ->
        {:ok, user}
      false ->
        {:error, changeset}
    end
  end
  #
  # @doc """
  #
  # """
  # @spec find_and_confirm_password(Repo, Ecto.Changeset) :: struct
  # def find_and_confirm_password(repo, changeset) do
  #   user = repo.get_by(ChitChat.User, username: changeset.params["username"])
  #   if user do
  #     if Bcrypt.checkpw(changeset.params["password"], user.hashed_password) do
  #       {:ok, user}
  #     else
  #       {:error, changeset}
  #     end
  #   else
  #     {:error, changeset}
  #   end
  # end
end
