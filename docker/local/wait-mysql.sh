#!/bin/sh

set -e

host="$1"
shift
port="$1"
shift
cmd="$@"

>&2 echo " Test connection on $host:$port"

until nc -z -v -w30 "$host" "$port"
do
  >&2 echo "Waiting for database connection..."
  sleep 1
done

>&2 echo "Mysql is now ready to accept connection, let's go to start tomcat container with command exec $cmd"

export JAVA_OPTS="$JAVA_OPTS -DMYSQL_DB_HOST=$MYSQL_DB_HOST -DMYSQL_DB_PORT=$MYSQL_DB_PORT -DMYSQL_DB_NAME=$MYSQL_DB_NAME -DMYSQL_USER=$MYSQL_USER -DMYSQL_PASSWORD=$MYSQL_PASSWORD"

>&2 echo "$JAVA_OPTS"

exec $cmd
