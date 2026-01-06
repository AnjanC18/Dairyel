$ErrorActionPreference = "Stop"

# Define paths
$projectRoot = $PSScriptRoot
$targetDir = Join-Path $projectRoot "target"
$jarName = "dairy-management-0.0.1-SNAPSHOT.jar"
$jarPath = Join-Path $targetDir $jarName

# Setup Environment (Reuse logic or minimal check)
# We assume java is available or we try to find it like in setup
if (-not $env:JAVA_HOME -and -not (Get-Command java -ErrorAction SilentlyContinue)) {
    $possibleJava = "C:\Users\anjan\.jdk\jdk-21.0.8"
    if (Test-Path $possibleJava) {
        $env:JAVA_HOME = $possibleJava
        $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    }
}

# Check if JAR exists
if (-not (Test-Path $jarPath)) {
    Write-Warning "JAR file not found at $jarPath. Attempting to build..."
    $setupScript = Join-Path $projectRoot "setup_and_build.ps1"
    if (Test-Path $setupScript) {
        & $setupScript
    }
    else {
        Write-Error "Build script not found. Cannot build project."
        exit 1
    }
}

# Run the application
Write-Host "Starting Dairy Management System..."
Write-Host "Press Ctrl+C to stop."
java -jar $jarPath
