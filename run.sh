#!/bin/bash

DIRECTORY=/budsdom/build/libs
PROJECT_NAME=buddy-wisdom

echo ">>> Move the directory."

cd $DIRECTORY

echo ">>> Verify that any processes are currently running."

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

if [ -z "$CURRENT_PID" ]; then
        echo ">>> No processes are currently running."
else
        echo ">>> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo ">>> Run the jar file"

JAR_NAME=$(find ./ -maxdepth 1 -type f -name "*.jar" -printf "%f\n" | sort -r | head -n 1)

echo ">>> JAR NAME : $JAR_NAME"

nohup java -jar "$JAR_NAME" > /dev/null 2>&1 &
