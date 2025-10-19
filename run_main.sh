#!/bin/bash
# Compile the project
mvn compile

# Run the main application
mvn exec:java -Dexec.mainClass="app.MainApp"
