#!/bin/bash
# Set permissions on the new files
chmod +x /opt/calculator/scripts/*.sh
chown -R ec2-user:ec2-user /opt/calculator

# Run a test calculation using your Main.java logic
# We will run '100 + 50' and save the output to a log file
cd /opt/calculator
java -jar maven-calculator-1.0-SNAPSHOT.jar 100 + 50 > /opt/calculator/last_run.log 2>&1
