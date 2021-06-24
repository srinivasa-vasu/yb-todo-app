ARG VERSION
FROM mcr.microsoft.com/vscode/devcontainers/universal:$VERSION

ARG YB_VERSION

USER root

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends

RUN curl -sSLo ./yugabyte.tar.gz https://downloads.yugabyte.com/yugabyte-${YB_VERSION}-linux.tar.gz \
	&& mkdir yugabyte \
    && tar -xvf yugabyte.tar.gz -C yugabyte --strip-components=1 \
    && mv ./yugabyte /usr/local/ \
    && chown -R codespace:codespace /usr/local/yugabyte \
    && ln -s /usr/local/yugabyte/bin/yugabyted /usr/local/bin/yugabyted \
    && ln -s /usr/local/yugabyte/bin/ysqlsh /usr/local/bin/ysqlsh \
    && chmod +x /usr/local/bin/yugabyted \
    && chmod +x /usr/local/bin/ysqlsh \
    && rm ./yugabyte.tar.gz

USER codespace

RUN echo "CREATE DATABASE todo;" > /usr/local/yugabyte/init-db.sql \
	&& echo "CREATE USER todo WITH PASSWORD 'todo';" >> /usr/local/yugabyte/init-db.sql \
	&& echo "GRANT ALL PRIVILEGES ON DATABASE todo TO todo;" >> /usr/local/yugabyte/init-db.sql \
	&& echo "\\c todo;" >> /usr/local/yugabyte/init-db.sql \
	&& echo "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";" >> /usr/local/yugabyte/init-db.sql

RUN echo "/usr/local/yugabyte/bin/post_install.sh" >> ~/.bashrc
RUN echo "yugabyted start --base_dir=/usr/local/yugabyte/ybd1 --listen=127.0.0.1" >> ~/.bashrc
RUN echo "test $? -eq 0 && sleep 0.5;ysqlsh -f /usr/local/yugabyte/init-db.sql || 'YugabyteDB is not running'" >> ~/.bashrc