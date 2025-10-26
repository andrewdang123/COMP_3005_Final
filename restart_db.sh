#!/bin/bash
# Delete the database and repopulate
rm data/testdb.mv.db
echo "Removed database"

# Populate Database
mvn clean compile
mvn exec:java -Dexec.mainClass="app.PopulateDatabase"
echo "Repopulated database"
