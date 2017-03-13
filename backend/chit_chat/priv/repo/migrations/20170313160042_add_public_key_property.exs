defmodule ChitChat.Repo.Migrations.AddPublicKeyProperty do
  use Ecto.Migration

  def change do
    alter table(:users) do
      add :public_key, :string, default: "", size: 1024
    end
  end
end
