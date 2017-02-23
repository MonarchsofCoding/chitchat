defmodule ChitChat.Message do
  @moduledoc """
  provides message entity.
  """

  use ChitChat.Web, :model

  alias ChitChat.UserRepository

  schema "messages" do
    field :recipient, :string, virtual: true
    belongs_to :destination, ChitChat.User
    belongs_to :source, ChitChat.User
    field :message, :string

    timestamps()
  end

  @spec changeset(struct, {}) :: struct
  def changeset(struct, params \\ %{}) do
    struct
    |> cast(params, [:recipient, :message])
    |> validate_required([:recipient, :message])
  end

  @spec valid_changeset(struct, {}) :: {}
  def valid_changeset(struct, params \\ %{}) do
    cs = changeset(struct, params)

    if cs.valid? do
      {:ok, cs}
    else
      {:error, cs}
    end
  end

  @spec create_with_participants(
    Ecto.Changeset, ChitChat.User, ChitChat.User) :: Message
  def create_with_participants(changeset, recipient, sender) do
    changeset = changeset

    |> change
    |> put_assoc(:destination, recipient)
    |> put_assoc(:source, sender)
    |> apply_changes

    {:ok, changeset}
  end

  @spec find_recipient(Ecto.Changeset) :: {}
  def find_recipient(changeset) do
    user = UserRepository.find_by_username(changeset.params["recipient"])

    if user != nil do
      {:ok, user}
    else
      changeset = changeset
      |> change
      |> add_error(:recipient, "does not exist")

      {:error, changeset}
    end
  end

end
