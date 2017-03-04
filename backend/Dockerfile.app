FROM erlang:slim

# HTTP
EXPOSE 80
# EPMD
EXPOSE 4369
#
EXPOSE 9100-9155

ENV PORT=80 MIX_ENV=prod REPLACE_OS_VARS=true SHELL=/bin/sh
ENV LC_ALL=en_GB.UTF-8

WORKDIR /opt/app

ADD chit_chat.tar.gz ./
# RUN chown -R default ./releases

# USER default

# ENTRYPOINT ["/opt/app/bin/chit_chat"]

CMD /bin/sh -c "export LOCAL_IP=`hostname -i` && /opt/app/bin/chit_chat foreground"
