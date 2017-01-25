defmodule ChitChat.Repo.Migrations.CreateUser do
  use Ecto.Migration

  def change do
    create table(:users) do
      add :username, :string
      add :hashed_password, :string

      timestamps()
    end

#   Create **unique** index on users.username
    create unique_index(:users, [:username])

  end
end
