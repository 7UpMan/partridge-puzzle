#!/bin/bash

# Run the PuzzleSolver main class
mvn compile -e exec:java -Dexec.mainClass="com.sevenUpMan.partridgePuzzle.PuzzleSolver" -Dexec.args="$*"
