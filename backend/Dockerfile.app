FROM erlang:slim

EXPOSE 80
ENV PORT=4000 MIX_ENV=prod REPLACE_OS_VARS=true SHELL=/bin/sh
ENV LC_ALL=en_GB.UTF-8

WORKDIR /opt/app

ADD chit_chat.tar.gz ./
# RUN chown -R default ./releases

# USER default

# ENTRYPOINT ["/opt/app/bin/chit_chat"]
