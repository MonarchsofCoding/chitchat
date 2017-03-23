FROM erlang:slim

# HTTP
EXPOSE 80
# EPMD
EXPOSE 4369
EXPOSE 9100-9155
EXPOSE 45892/udp

ENV PORT=80
ENV MIX_ENV=prod
ENV REPLACE_OS_VARS=true
ENV SHELL=/bin/bash

RUN apt-get clean && apt-get update \
    && apt-get install locales
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN apt-get autoremove && rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app

ADD chit_chat.tar.gz ./
# RUN chown -R default ./releases

# USER default

# Add wait-for-it
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /bin/wait-for-it.sh
RUN chmod +x /bin/wait-for-it.sh

# Add entrypoint
COPY chit_chat/rel/entrypoint.sh /opt/app/entrypoint.sh
RUN chmod +x /opt/app/entrypoint.sh

ENTRYPOINT ["/opt/app/entrypoint.sh"]

CMD foreground
