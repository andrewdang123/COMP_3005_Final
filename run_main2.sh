#!/bin/bash
# Compile the project
mvn compile

# Run the main application with custom logging config 
mvn exec:java \
  -Dexec.mainClass="app.MainApp" \
  -Djava.util.logging.config.file=src/main/resources/logging.properties
