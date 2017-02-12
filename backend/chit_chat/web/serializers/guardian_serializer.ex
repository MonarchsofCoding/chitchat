defmodule ChitChat.GuardianSerializer do
  @moduledoc """
  A module for use with the Guardian JWT library
  """
  @behaviour Guardian.Serializer

  alias ChitChat.Repo
  alias ChitChat.User

  @spec for_token(User) :: struct
  def for_token(user = %User{}) do
    {:ok, "User:#{user.id}"}
  end

  @spec for_token(any) :: struct
  def for_token(_) do
    {:error, "Unknown resource type"}
  end

  @spec from_token(any) :: struct
  def from_token("User:" <> id) do
    {:ok, Repo.get(User, id)}
  end

  @spec from_token(any) :: struct
  def from_token(_) do
    {:error, "Unknown resource type"}
  end

end
