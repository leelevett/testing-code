#!/usr/bin/env bash

while read -r line;
do
  if [[ $line =~ \= ]]; then
    rhs=${line#*=}
    lhs=${line%=*}
    export $lhs="$rhs"
  fi
done < <(curl --silent --get http://some.server.co.uk/props)