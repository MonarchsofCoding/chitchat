defmodule ChitChat.MessageChannel do
  @moduledoc """
  provides the message channel for users
  """

  use Phoenix.Channel
  import Guardian.Phoenix.Socket

  alias Guardian.Phoenix.Socket

  @spec join({}, {}, {}) :: {}
  def join(_room, %{"guardian_token" => token}, socket) do
    case sign_in(socket, token) do
      {:ok, authed_socket, _guardian_params} ->
        user = Socket.current_resource(authed_socket)

        {:ok, %{message: "Joined"}, authed_socket}
      {:error, reason} ->
        # handle error
    end
  end

  @spec join({}, {}, {}) :: {}
  def join(room, _, socket) do
    {:error,  :authentication_required}
  end

  @spec handle_in(String, {}, {}) :: {}
  def handle_in("ping", _payload, socket) do
    user = current_resource(socket)
    broadcast(socket, "pong", %{message: "pong", from: user.username})
    {:noreply, socket}
  end
end
