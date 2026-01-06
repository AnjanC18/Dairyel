$ErrorActionPreference = "Stop"

# Define paths relative to the script location
$projectRoot = $PSScriptRoot
$toolsDir = Join-Path $projectRoot "tools"
$mavenVersion = "3.9.6"
$mavenHome = Join-Path $toolsDir "apache-maven-$mavenVersion"
$mavenBin = Join-Path $mavenHome "bin"

# Create tools directory if it doesn't exist
if (-not (Test-Path $toolsDir)) {
    New-Item -ItemType Directory -Force -Path $toolsDir | Out-Null
}

# Download and install Maven if not present
if (-not (Test-Path $mavenHome)) {
    $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    $zipPath = Join-Path $toolsDir "maven.zip"
    
    Write-Host "Downloading Maven $mavenVersion..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $zipPath -DestinationPath $toolsDir -Force
    
    Remove-Item $zipPath
}

# Setup Environment Variables
# 1. Add Maven to PATH
$env:PATH = "$mavenBin;$env:PATH"

# 2. Check for JAVA_HOME or java in PATH
if (-not $env:JAVA_HOME) {
    # Try to find java in PATH
    if (Get-Command java -ErrorAction SilentlyContinue) {
        Write-Host "Using Java from PATH"
    } else {
        # Fallback to known locations if needed, or error out
        # Trying the user's previously seen location as a fallback hint
        $possibleJava = "C:\Users\anjan\.jdk\jdk-21.0.8"
        if (Test-Path $possibleJava) {
             $env:JAVA_HOME = $possibleJava
             $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
             Write-Host "Set JAVA_HOME to $possibleJava"
        } else {
             Write-Warning "JAVA_HOME is not set and 'java' is not in PATH. Build might fail."
        }
    }
} else {
    Write-Host "Using JAVA_HOME: $env:JAVA_HOME"
}

# Verify installation
Write-Host "Verifying Maven installation..."
mvn -version

# Run build
Write-Host "Building project..."
mvn clean install -DskipTests
