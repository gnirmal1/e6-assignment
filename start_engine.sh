#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: ./start_engine.sh <port_number>"
    exit 1
fi

PORT=$1

# Compile the Engine class
javac -d out src/org/e6/engine/Engine.java src/org/e6/engine/EngineResult.java

# Run the Engine
java -cp out org.e6.engine.Engine "$PORT"
