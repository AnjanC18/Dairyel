$ErrorActionPreference = "Stop"

$minGitVersion = "2.43.0"
$minGitUrl = "https://github.com/git-for-windows/git/releases/download/v$minGitVersion.windows.1/MinGit-$minGitVersion-64-bit.zip"
$projectRoot = $PSScriptRoot
$installDir = Join-Path $projectRoot "tools"
$gitHome = "$installDir\MinGit"
$gitBin = "$gitHome\cmd"

# Create tools directory
if (-not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Force -Path $installDir | Out-Null
}

# Download MinGit if not exists
$zipPath = "$installDir\mingit.zip"
if (-not (Test-Path $gitHome)) {
    Write-Host "Downloading MinGit $minGitVersion..."
    Invoke-WebRequest -Uri $minGitUrl -OutFile $zipPath
    
    Write-Host "Extracting MinGit..."
    Expand-Archive -Path $zipPath -DestinationPath $gitHome -Force
    
    Remove-Item $zipPath
}

# Set environment variables for this session
$env:PATH = "$gitBin;$env:PATH"

Write-Host "MinGit setup complete."
Write-Host "Git Home: $gitHome"

# Verify installation
git --version
