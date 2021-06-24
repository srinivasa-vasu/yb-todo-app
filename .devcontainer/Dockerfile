# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.158.0/containers/codespaces-linux/.devcontainer/base.Dockerfile
ARG version=1-focal

FROM mcr.microsoft.com/vscode/devcontainers/universal:$version

USER root

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends

RUN curl -sSLo ./yugabyte.tar.gz https://downloads.yugabyte.com/yugabyte-2.7.1.1-darwin.tar.gz \
    && tar -xvf yugabyte.tar.gz yugabyte \
    && mv -r ./yugabyte /usr/local/ \
    && ln -s /usr/local/yugabyte/bin/yugabyted /usr/local/bin/yugabyted \
    && ln -s /usr/local/yugabyte/bin/ycqlsh /usr/local/bin/ycqlsh \
    && chmod +x /usr/local/bin/yugabyted \
    && chmod +x /usr/local/bin/ycqlsh \
    && rm ./yugabyte.tar.gz

USER codespace

RUN yugabyted start --base_dir=/tmp/ybd1 --listen=127.0.0.1

RUN cat > /tmp/init-db.sql << EOF
CREATE DATABASE todo;
CREATE USER todo WITH PASSWORD 'todo';
GRANT ALL PRIVILEGES ON DATABASE todo TO todo;
\c todo;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOF

ysqlsh -f /tmp/init-db.sql