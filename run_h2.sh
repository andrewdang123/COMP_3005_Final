#!/bin/bash
# Compile the project and run H2 Console
mvn compile exec:java -Dexec.mainClass="app.H2Console"
