FROM alpine:latest

RUN apk --update add \
  elixir \
  postgresql-client \
  erlang-crypto \
  erlang-parsetools \
  erlang-syntax-tools \
  build-base

ENV APP_NAME chit_chat

RUN mkdir -p /${APP_NAME}

COPY ${APP_NAME}/config /${APP_NAME}/config
COPY ${APP_NAME}/lib /${APP_NAME}/lib
COPY ${APP_NAME}/priv /${APP_NAME}/priv
COPY ${APP_NAME}/web /${APP_NAME}/web
COPY ${APP_NAME}/mix.exs /${APP_NAME}
COPY ${APP_NAME}/mix.lock /${APP_NAME}

WORKDIR /${APP_NAME}

ENV MIX_ENV prod
ENV PORT 4001

RUN mix local.hex --force
RUN mix local.rebar --force

RUN mix deps.get

RUN mix compile

RUN apk del build-base
RUN rm -rf /var/cache/apk/*

CMD trap exit TERM; mix ecto.create && mix ecto.migrate && mix phoenix.server
