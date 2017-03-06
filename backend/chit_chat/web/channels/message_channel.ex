defmodule ChitChat.MessageChannel do
  @moduledoc """
  provides the message channel for users
  """

  use Phoenix.Channel
  import Guardian.Phoenix.Socket

  alias Guardian.Phoenix.Socket
  alias ChitChat.Presence

  @spec join({}, Map, Phoenix.Socket) :: {}
  def join(_room, %{"authToken" => token}, socket) do
    case sign_in(socket, token) do
      {:ok, authed_socket, _guardian_params} ->
        user = Socket.current_resource(authed_socket)
        send(self(), :after_join)
        {:ok, %{message: "Joined user:#{user.username}"}, authed_socket}
      {:error, reason} ->
        # handle error
        {:error, reason}
    end
  end

  @spec join({}, {}, Phoenix.Socket) :: {}
  def join(_room, _, _socket) do
    {:error,  :authentication_required}
  end

  def handle_info(:after_join, socket) do
    push socket, "presence_state", Presence.list(socket)
    user = Socket.current_resource(socket)
    {:ok, _} = Presence.track(socket, user.id, %{
      online_at: inspect(System.system_time(:seconds))
    })
    {:noreply, socket}
  end

end
