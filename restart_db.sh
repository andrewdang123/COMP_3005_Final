#!/bin/bash
# Delete the database and repopulate
rm data/testdb.mv.db
echo "Removed database"

# Populate Databas
mvn exec:java -Dexec.mainClass="app.PopulateDatabase"
echo "Repopulated database"
