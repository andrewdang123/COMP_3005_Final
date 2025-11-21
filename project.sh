#!/bin/bash

# Delete the database and repopulate
rm data/testdb.mv.db
echo "Removed database"

# Populate Database
mvn clean compile
mvn exec:java -Dexec.mainClass="app.PopulateDatabase"
echo "Repopulated database"


# Run the main application with custom logging config 
mvn exec:java \
  -Dexec.mainClass="app.MainApp" \
  -Djava.util.logging.config.file=src/main/resources/logging.properties
