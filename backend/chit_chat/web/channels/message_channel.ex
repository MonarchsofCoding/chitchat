defmodule ChitChat.MessageChannel do
  use Phoenix.Channel
  import Guardian.Phoenix.Socket

  def join(_room, %{"guardian_token" => token}, socket) do
    IO.inspect("JOIN with guardian")
    case sign_in(socket, token) do
      {:ok, authed_socket, _guardian_params} ->
        user = Guardian.Phoenix.Socket.current_resource(authed_socket)
        IO.inspect(user)

        {:ok, %{message: "Joined"}, authed_socket}
      {:error, reason} ->
        # handle error
    end
  end

  def join(room, _, socket) do
    IO.inspect("JOIN")
    {:error,  :authentication_required}
  end

  def handle_in("ping", _payload, socket) do
    IO.inspect("HANDLE_IN")
    user = current_resource(socket)
    broadcast(socket, "pong", %{message: "pong", from: user.username})
    {:noreply, socket}
  end
end
