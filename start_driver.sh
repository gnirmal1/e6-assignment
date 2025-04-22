#!/bin/bash

if [ $# -ne 3 ]; then
    echo "Usage: ./start_driver.sh <port1> <port2> <port3>"
    exit 1
fi

PORT1=$1
PORT2=$2
PORT3=$3

# Compile the Driver and dependent files
javac -d out src/org/e6/driver/Driver.java src/org/e6/engine/EngineTask.java src/org/e6/engine/EngineResult.java

# Run the Driver
java -cp out org.e6.driver.Driver "$PORT1" "$PORT2" "$PORT3"
