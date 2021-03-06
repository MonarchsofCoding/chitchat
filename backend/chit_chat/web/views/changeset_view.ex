defmodule ChitChat.ChangesetView do
  @moduledoc """
  provides the error json translations.
  """
  use ChitChat.Web, :view

  alias Ecto.Changeset

  @doc """
  Traverses and translates changeset errors.

  See `Ecto.Changeset.traverse_errors/2` and
  `ChitChat.ErrorHelpers.translate_error/1` for more details.
  """
  @spec translate_errors({}) :: {}
  def translate_errors(changeset) do
    Changeset.traverse_errors(changeset, &translate_error/1)
  end

  @spec render(String, {}) :: {}
  def render("error.json", %{changeset: changeset}) do
    # When encoded, the changeset returns its errors
    # as a JSON object. So we just pass it forward.
    %{errors: translate_errors(changeset)}
  end
end
