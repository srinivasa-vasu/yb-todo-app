ARG VERSION
FROM mcr.microsoft.com/vscode/devcontainers/universal:$VERSION

ARG YB_VERSION
ARG ROLE

USER root

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends

RUN curl -sSLo ./yugabyte.tar.gz https://downloads.yugabyte.com/yugabyte-${YB_VERSION}-linux.tar.gz \
	&& mkdir yugabyte \
    && tar -xvf yugabyte.tar.gz -C yugabyte --strip-components=1 \
    && mv ./yugabyte /usr/local/ \
    && ln -s /usr/local/yugabyte/bin/yugabyted /usr/local/bin/yugabyted \
    && ln -s /usr/local/yugabyte/bin/ysqlsh /usr/local/bin/ysqlsh \
    && chmod +x /usr/local/bin/yugabyted \
    && chmod +x /usr/local/bin/ysqlsh \
    && rm ./yugabyte.tar.gz

RUN mkdir -p /var/ybdp/data \
	&& chown -R $ROLE:$ROLE /var/ybdp/data \
	&& chown -R $ROLE:$ROLE /usr/local/yugabyte

USER $ROLE

RUN echo "CREATE DATABASE todo;" > /var/ybdp/data/init-db.sql \
	&& echo "CREATE USER todo WITH PASSWORD 'todo';" >> /var/ybdp/data/init-db.sql \
	&& echo "GRANT ALL PRIVILEGES ON DATABASE todo TO todo;" >> /var/ybdp/data/init-db.sql \
	&& echo '\\c todo;' >> /var/ybdp/data/init-db.sql \
	&& echo "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";" >> /var/ybdp/data/init-db.sql

RUN echo "/usr/local/yugabyte/bin/post_install.sh 2>&1" >> ~/.bashrc
RUN echo "yugabyted start --base_dir=/var/ybdp/data/ybd1 --listen=127.0.0.1" >> ~/.bashrc
RUN echo "[[ ! -f /var/ybdp/data/.init-db.sql.completed ]] && ([[ $? -eq 0 ]] && (sleep 1;ysqlsh -f /var/ybdp/data/init-db.sql;touch /var/ybdp/data/.init-db.sql.completed) || echo 'YugabyteDB is not running!')" >> ~/.bashrc