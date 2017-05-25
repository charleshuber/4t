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
  >&2 echo "Waiting for tomcat availability..."
  sleep 1
done

>&2 echo "Tomcat is now ready to accept connection, let's go to start Apache container with command exec $cmd"
exec $cmd
