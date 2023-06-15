#!/bin/bash

IS_GREEN=$(docker ps | grep green)
DEFAULT_CONF=" /etc/nginx/nginx.conf"

if [ -z $IS_GREEN  ];then

  echo ">>> Green container up"

  docker-compose pull green
  docker-compose up -d green

  while [ 1 = 1 ]; do
    echo ">>> Health check"
    sleep 3

    REQUEST=$(curl http://127.0.0.1:8081)

    if [ -n "$REQUEST" ]; then
            echo ">>> Health check success"
            break ;
            fi
  done;

  echo ">>> Reload nginx"
  sudo cp /etc/nginx/nginx.green.conf $DEFAULT_CONF
  sudo nginx -s rel

  echo ">>> Blue container down"
  docker-compose stop blue
else

  echo ">>> Blue container up"

  docker-compose pull blue
  docker-compose up -d blue

  while [ 1 = 1 ]; do
    echo ">>> Health check"
    sleep 3

    REQUEST=$(curl http://127.0.0.1:8080)

    if [ -n "$REQUEST" ]; then
      echo ">>> Health check success"
      break ;
    fi
  done;

  echo ">>> Reload nginx"
  sudo cp /etc/nginx/nginx.blue.conf $DEFAULT_CONF
  sudo nginx -s reload

  echo ">>> Green container down"
  docker-compose stop green
fi
