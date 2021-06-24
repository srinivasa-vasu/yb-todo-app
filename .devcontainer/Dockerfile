FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

RUN if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

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

USER vscode

RUN yugabyted start --base_dir=/tmp/ybd1 --listen=127.0.0.1

RUN echo 'CREATE DATABASE todo;' > /tmp/init-db.sql \
	&& echo 'CREATE USER todo WITH PASSWORD "todo";' >> /tmp/init-db.sql \
	&& echo 'GRANT ALL PRIVILEGES ON DATABASE todo TO todo;' >> /tmp/init-db.sql \
	&& echo '\c todo;' >> /tmp/init-db.sql \
	&& echo 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";' >> /tmp/init-db.sql

RUN ysqlsh -f /tmp/init-db.sql