defmodule ChitChat.Repo.Migrations.AddPublicKeyProperty do
  use Ecto.Migration

  def change do
    alter table(:users) do
      add :public_key, :string, default: ""
    end
  end
end
