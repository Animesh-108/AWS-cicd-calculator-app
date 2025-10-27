#!/bin/bash
# Clean up the log from the last run
rm -f /opt/calculator/last_run.log
# Ensure the directories exist
mkdir -p /opt/calculator
mkdir -p /opt/calculator/scripts
echo "Cleanup complete."
