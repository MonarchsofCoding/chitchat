FROM bitwalker/alpine-erlang:6.1

RUN apk update && \
    apk --no-cache --update add libgcc libstdc++ && \
    rm -rf /var/cache/apk/*

EXPOSE 80
ENV PORT=4000 MIX_ENV=prod REPLACE_OS_VARS=true SHELL=/bin/sh

ADD chit_chat.tar.gz ./
RUN chown -R default ./releases

# USER default

# ENTRYPOINT ["/opt/app/bin/chit_chat"]
