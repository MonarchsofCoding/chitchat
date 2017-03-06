defmodule ChitChat.Presence do
  use Phoenix.Presence, otp_app: :chit_chat,
                        pubsub_server: ChitChat.PubSub
end
