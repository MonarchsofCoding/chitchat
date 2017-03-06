defmodule ChitChat.Presence do
  @moduledoc """
  provides presence for the pubsub.
  """
  use Phoenix.Presence, otp_app: :chit_chat,
                        pubsub_server: ChitChat.PubSub
end
