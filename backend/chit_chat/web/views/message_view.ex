defmodule ChitChat.MessageView do
  use ChitChat.Web, :view

  @spec render(String, {}) :: {}
  def render("index.json", %{messages: messages}) do
    %{data: render_many(messages, ChitChat.MessageView, "message.json")}
  end

  @spec render(String, {}) :: {}
  def render("show.json", %{message: message}) do
    %{data: render_one(message, ChitChat.MessageView, "message.json")}
  end

  @spec render(String, {}) :: {}
  def render("message.json", %{message: message}) do
    %{
      recipient: message.destination.username,
      sender: message.source.username,
      message: message.message
    }
  end
end
