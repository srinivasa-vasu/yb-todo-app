FROM gitpod/workspace-full

ARG YB_VERSION=2.7.1.1
ARG ROLE=gitpod

USER root

RUN apt-get update && \
	apt-get -y install netcat --no-install-recommends

RUN curl -sSLo ./yugabyte.tar.gz https://downloads.yugabyte.com/yugabyte-${YB_VERSION}-linux.tar.gz \
	&& mkdir yugabyte \
    && tar -xvf yugabyte.tar.gz -C yugabyte --strip-components=1 \
    && mv ./yugabyte /usr/local/ \
    && ln -s /usr/local/yugabyte/bin/yugabyted /usr/local/bin/yugabyted \
    && ln -s /usr/local/yugabyte/bin/ysqlsh /usr/local/bin/ysqlsh \
    && chmod +x /usr/local/bin/yugabyted \
    && chmod +x /usr/local/bin/ysqlsh \
    && rm ./yugabyte.tar.gz

RUN mkdir -p /var/ybdp \
	&& chown -R $ROLE:$ROLE /var/ybdp \
	&& chown -R $ROLE:$ROLE /usr/local/yugabyte

USER $ROLE

ENV STORE=/var/ybdp
ENV LISTEN=127.0.0.1
ENV PORT=5433

RUN echo "CREATE DATABASE todo;" > $STORE/init-db.sql \
	&& echo "CREATE USER todo WITH PASSWORD 'todo';" >> $STORE/init-db.sql \
	&& echo "GRANT ALL PRIVILEGES ON DATABASE todo TO todo;" >> $STORE/init-db.sql \
	&& echo '\\c todo;' >> $STORE/init-db.sql \
	&& echo "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";" >> $STORE/init-db.sql