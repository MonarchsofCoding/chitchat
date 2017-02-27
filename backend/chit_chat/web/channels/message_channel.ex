defmodule HelloPhoenix.RoomChannel do
  use Phoenix.Channel
  import Guardian.Phoenix.Socket

  def join(_room, %{"guardian_token" => token}, socket) do
    case sign_in(socket, token) do
      {:ok, authed_socket, _guardian_params} ->
        {:ok, %{message: "Joined"}, authed_socket}
        # user = Guardian.Phoenix.Socket.current_resource(authed_socket)
      {:error, reason} ->
        # handle error
    end
  end

  def join(room, _, socket) do
    {:error,  :authentication_required}
  end

  def handle_in("ping", _payload, socket) do
    user = current_resource(socket)
    broadcast(socket, "pong", %{message: "pong", from: user.username})
    {:noreply, socket}
  end
end
