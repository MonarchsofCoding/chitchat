defmodule ChitChat.MessageSocket do
  @moduledoc """
  provides a MessageSocket.
  """

  use Phoenix.Socket
  use Guardian.Phoenix.Socket

  # Channels
  # channel "room:*", ChitChat.RoomChannel
  channel "message:*", HelloPhoenix.RoomChannel

  # Transports
  transport :websocket, Phoenix.Transports.WebSocket
  # transport :longpoll, Phoenix.Transports.LongPoll

  # Socket params are passed from the client and can
  # be used to verify and authenticate a user. After
  # verification, you can put default assigns into
  # the socket that will be set for all channels, ie
  #
  #     {:ok, assign(socket, :user_id, verified_user_id)}
  #
  # To deny connection, return `:error`.
  #
  # See `Phoenix.Token` documentation for examples in
  # performing token verification on connect.
  @spec connect({}, Socket) :: Socket
  def connect(_params, socket) do
    # if we get here, we did not authenticate
    {:error}
  end

  def connect(%{"guardian_token" => jwt} = params, socket) do
    case sign_in(socket, jwt) do
      {:ok, authed_socket, guardian_params} ->
        {:ok, authed_socket}
      _ ->
          user = Guardian.Phoenix.Socket.current_resource(socket)
          IO.inspect(user)
        #unauthenticated socket
        {:ok, socket}
    end
  end

  # Socket id's are topics that allow you to identify all sockets
  #    for a given user:
  #
  #     def id(socket), do: "users_socket:#{socket.assigns.user_id}"
  #
  # Would allow you to broadcast a "disconnect" event
  # and terminate all active sockets and channels for
  # a given user:
  #
  #     ChitChat.Endpoint.broadcast(
  #     "users_socket:#{user.id}", "disconnect", %{})
  #
  # Returning `nil` makes this socket anonymous.
  @spec id(Socket) :: nil
  def id(_socket), do: nil
end
