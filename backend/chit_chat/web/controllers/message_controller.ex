defmodule ChitChat.MessageController do
  use ChitChat.Web, :controller
  use Guardian.Phoenix.Controller

  alias ChitChat.User
  alias ChitChat.Message
  alias ChitChat.ChangesetView
  alias ChitChat.AuthController
  alias ChitChat.ErrorView

  @doc """
  Creates a new Message with the given parameters
  """
  @spec create(Conn, {}, User, {}) :: nil
  def create(conn, message_params, user, _claims) do

    with {:ok, user} <- AuthController.authenticate(user),
         {:ok, changeset} <- Message.valid_changeset(
                                      %Message{}, message_params),
         {:ok, recipient} <- Message.find_recipient(changeset),
         {:ok, message} <- Message.create_with_participants(
                                    changeset, recipient, user)
    do
      conn
      |> put_status(:created)
      |> render("show.json", message: message)
    else
      {:error, status} when is_atom(status) ->
        conn
        |> put_status(status)
        |> render(ErrorView, "status.json", status: status)
      {:error, changeset}  ->
        conn
        |> put_status(:unprocessable_entity)
        |> render(ChangesetView, "error.json", changeset: changeset)
    end

  end

end
