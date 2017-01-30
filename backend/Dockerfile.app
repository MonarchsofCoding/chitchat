FROM elixir:latest

ENV APP_NAME chit_chat
ENV APP_VERSION "0.0.1"
ENV REPLACE_OS_VARS=true

RUN mkdir -p /${APP_NAME}
ADD ${APP_NAME}/_build/prod/rel/${APP_NAME}/releases/${APP_VERSION}/${APP_NAME}.tar.gz /${APP_NAME}/

RUN ls -l /${APP_NAME}/bin

WORKDIR /
CMD trap exit TERM; /${APP_NAME}/bin/${APP_NAME} foreground || cat /erl_crash.dump
