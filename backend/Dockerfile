FROM alpine:latest

# RUN apk --update add openssl erlang

COPY chit_chat/_build/prod/rel/chit_chat/ /app/

CMD /app/bin/chit_chat foreground
