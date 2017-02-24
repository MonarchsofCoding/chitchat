defmodule ChitChat.Router do
  use ChitChat.Web, :router

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_flash
    plug :protect_from_forgery
    plug :put_secure_browser_headers
  end

  pipeline :api do
    plug :accepts, ["json"]
    plug Guardian.Plug.VerifyHeader, realm: "Bearer"
    plug Guardian.Plug.LoadResource
  end

  scope "/", ChitChat do
    pipe_through :browser # Use the default browser stack

    get "/", PageController, :index
  end

  scope "/api/v1", ChitChat do
    pipe_through :api # Use the API stack

    resources "/users", UserController, only: [:create, :show, :index]
    resources "/auth", AuthController, only: [:create]
  end

end
