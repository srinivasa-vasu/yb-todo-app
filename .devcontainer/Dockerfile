ARG VARIANT=11
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

ARG INSTALL_GRADLE="true"
ARG GRADLE_VERSION="7.1"
RUN if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

USER root

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends

RUN curl -sSLo ./yugabyte.tar.gz https://downloads.yugabyte.com/yugabyte-2.7.1.1-darwin.tar.gz \
	&& mkdir yugabyte \
    && tar -xvf yugabyte.tar.gz -C yugabyte --strip-components=1 \
    && mv ./yugabyte /usr/local/ \
    && ln -s /usr/local/yugabyte/bin/yugabyted /usr/local/bin/yugabyted \
    && ln -s /usr/local/yugabyte/bin/ycqlsh /usr/local/bin/ycqlsh \
    && chmod +x /usr/local/bin/yugabyted \
    && chmod +x /usr/local/bin/ycqlsh \
    && rm ./yugabyte.tar.gz

USER vscode

RUN yugabyted start --base_dir=/tmp/ybd1 --listen=127.0.0.1

RUN echo 'CREATE DATABASE todo;' > /tmp/init-db.sql \
	&& echo 'CREATE USER todo WITH PASSWORD "todo";' >> /tmp/init-db.sql \
	&& 'GRANT ALL PRIVILEGES ON DATABASE todo TO todo;' >> /tmp/init-db.sql \
	&& '\c todo;' >> /tmp/init-db.sql \
	&& 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";' >> /tmp/init-db.sql

RUN ysqlsh -f /tmp/init-db.sql