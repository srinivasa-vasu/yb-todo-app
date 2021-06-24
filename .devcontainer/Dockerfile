ARG VERSION
FROM mcr.microsoft.com/vscode/devcontainers/universal:$VERSION

ARG YB_VERSION
ARG ROLE

USER root

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install netcat --no-install-recommends

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
ARG STORE=/var/ybdp
ARG LISTEN=127.0.0.1
ARG PORT=5433

RUN echo "CREATE DATABASE todo;" > $STORE/init-db.sql \
	&& echo "CREATE USER todo WITH PASSWORD 'todo';" >> $STORE/init-db.sql \
	&& echo "GRANT ALL PRIVILEGES ON DATABASE todo TO todo;" >> $STORE/init-db.sql \
	&& echo '\\c todo;' >> $STORE/init-db.sql \
	&& echo "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";" >> $STORE/init-db.sql

RUN echo "/usr/local/yugabyte/bin/post_install.sh 2>&1" >> ~/.bashrc
RUN echo "yugabyted start --base_dir=$STORE/ybd1 --listen=$LISTEN" >> ~/.bashrc
RUN echo "[[ ! -f $STORE/.init-db.sql.completed ]] && " \
	"{ for i in {1..5}; do (nc -vz $LISTEN $PORT >/dev/null 2>&1); [[ \$? -eq 0 ]] && "\
	"{ ysqlsh -f $STORE/init-db.sql; touch $STORE/.init-db.sql.completed; break; } || sleep \$i; done }" >> ~/.bashrc
RUN echo "[[ ! -f $STORE/.init-db.sql.completed ]] && echo 'YugabyteDB is not running!'"	>> ~/.bashrc