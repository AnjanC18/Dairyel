$ErrorActionPreference = "Stop"

# Set environment variables for this session
$env:JAVA_HOME = "C:\Users\anjan\.jdk\jdk-21.0.8"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Starting Dairy Management System..."
# Run the packaged jar directly
java -jar target/dairy-management-0.0.1-SNAPSHOT.jar
