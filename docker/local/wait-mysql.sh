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
exec $cmd
