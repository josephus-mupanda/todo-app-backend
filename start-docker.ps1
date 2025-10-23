# ============================
# start-docker.ps1
# ============================

Write-Host "Setting up Docker environment using Docker Toolbox..." -ForegroundColor Cyan

# ============================
# Config
# ============================
$dockerMachineExe = "C:\Program Files\Docker Toolbox\docker-machine.exe"
$machineName = "default"
$projectDir = "C:\Users\josep\Development\My projects\todo_app"
$envFile = Join-Path $projectDir ".env"

# ============================
# Check and start Docker Machine
# ============================
Write-Host "Checking Docker Machine status..." -ForegroundColor Yellow

try {
    $machineStatus = & $dockerMachineExe status $machineName 2>&1

    if ($machineStatus -match "Running") {
        Write-Host "Docker Machine '$machineName' is already running." -ForegroundColor Green
    } else {
        Write-Host "Starting Docker Machine '$machineName'..." -ForegroundColor Yellow
        & $dockerMachineExe start $machineName
        Start-Sleep -Seconds 5
    }
} catch {
    Write-Host "Error checking Docker Machine: $_" -ForegroundColor Red
    exit 1
}

# ============================
# Configure Docker environment
# ============================
Write-Host "Configuring Docker environment variables..." -ForegroundColor Cyan

try {
    $envOutput = & $dockerMachineExe env --shell powershell $machineName 2>&1
    if ($LASTEXITCODE -eq 0) {
        $envOutput | Invoke-Expression
        Write-Host "Docker environment configured successfully." -ForegroundColor Green
    } else {
        Write-Host "Warning: Docker environment configuration may have issues" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error configuring Docker environment: $_" -ForegroundColor Red
}

# ============================
# Get Docker Machine IP
# ============================
Write-Host "Getting Docker Machine IP..." -ForegroundColor Cyan

try {
    $dockerIp = (& $dockerMachineExe ip $machineName 2>&1).Trim()

    if ($dockerIp -match '^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$') {
        Write-Host "Docker Machine IP: $dockerIp" -ForegroundColor Green
    } else {
        Write-Host "Failed to get valid IP address. Got: $dockerIp" -ForegroundColor Red
        Write-Host "Using fallback IP: 192.168.99.100" -ForegroundColor Yellow
        $dockerIp = "192.168.99.100"
    }
} catch {
    Write-Host "Error getting Docker IP: $_" -ForegroundColor Red
    Write-Host "Using fallback IP: 192.168.99.100" -ForegroundColor Yellow
    $dockerIp = "192.168.99.100"
}

# ============================
# Update .env file dynamically
# ============================
Write-Host "Updating .env file with IP: $dockerIp" -ForegroundColor Cyan

if (Test-Path $envFile) {
    try {
        $content = Get-Content $envFile -Raw

        # Update DATABASE_HOST
        $content = $content -replace 'DATABASE_HOST=.*', "DATABASE_HOST=$dockerIp"

        # Write back to file
        $content | Set-Content $envFile -NoNewline

        Write-Host "Successfully updated .env file" -ForegroundColor Green
        Write-Host "  - DATABASE_HOST=$dockerIp" -ForegroundColor Gray
    } catch {
        Write-Host "Error updating .env file: $_" -ForegroundColor Red
    }
} else {
    Write-Host ".env file not found at: $envFile" -ForegroundColor Red
    exit 1
}

# ============================
# Navigate to project directory
# ============================
Set-Location $projectDir

# ============================
# Start Docker Compose
# ============================
Write-Host ""
Write-Host "Starting Docker Compose services..." -ForegroundColor Cyan

try {
    docker-compose -f docker-compose.yml up -d

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "=====================================" -ForegroundColor Green
        Write-Host "  Docker services started successfully!" -ForegroundColor Green
        Write-Host "=====================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Service Endpoints:" -ForegroundColor Cyan
        Write-Host "  Postgres: ${dockerIp}:5433" -ForegroundColor White
        Write-Host ""
        Write-Host "Your .env file has been updated with:" -ForegroundColor Yellow
        Write-Host "  DATABASE_HOST=${dockerIp}" -ForegroundColor Gray
        Write-Host ""
        Write-Host "You can now run your Spring Boot application." -ForegroundColor Cyan
        Write-Host ""
    } else {
        Write-Host "Docker Compose encountered issues (exit code: $LASTEXITCODE)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error starting Docker Compose: $_" -ForegroundColor Red
    exit 1
}

# ============================
# Verify containers are running
# ============================
Write-Host "Verifying containers..." -ForegroundColor Cyan
docker ps --filter "name=todo-" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"


## ============================
## start-docker.ps1 - FIXED VERSION
## ============================
#
#Write-Host "Setting up Docker environment using Docker Toolbox..." -ForegroundColor Cyan
#
## ============================
## Config
## ============================
#$dockerMachineExe = "C:\Program Files\Docker Toolbox\docker-machine.exe"
#$machineName = "default"
#$projectDir = "C:\Users\josep\Development\My projects\todo_app"
#$envFile = Join-Path $projectDir ".env"
#
## ============================
## Check and start Docker Machine
## ============================
#Write-Host "Checking Docker Machine status..." -ForegroundColor Yellow
#
#try {
#    $machineStatus = & $dockerMachineExe status $machineName 2>&1
#
#    if ($machineStatus -match "Running") {
#        Write-Host "Docker Machine '$machineName' is already running." -ForegroundColor Green
#    } else {
#        Write-Host "Starting Docker Machine '$machineName'..." -ForegroundColor Yellow
#        & $dockerMachineExe start $machineName
#        Start-Sleep -Seconds 5
#    }
#} catch {
#    Write-Host "Error checking Docker Machine: $_" -ForegroundColor Red
#    exit 1
#}
#
## ============================
## Configure Docker environment
## ============================
#Write-Host "Configuring Docker environment variables..." -ForegroundColor Cyan
#
#try {
#    $envOutput = & $dockerMachineExe env --shell powershell $machineName 2>&1
#    if ($LASTEXITCODE -eq 0) {
#        $envOutput | Invoke-Expression
#        Write-Host "Docker environment configured successfully." -ForegroundColor Green
#    } else {
#        Write-Host "Warning: Docker environment configuration may have issues" -ForegroundColor Yellow
#    }
#} catch {
#    Write-Host "Error configuring Docker environment: $_" -ForegroundColor Red
#}
#
## ============================
## Get Docker Machine IP
## ============================
#Write-Host "Getting Docker Machine IP..." -ForegroundColor Cyan
#
#try {
#    $dockerIp = (& $dockerMachineExe ip $machineName 2>&1).Trim()
#
#    if ($dockerIp -match '^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$') {
#        Write-Host "Docker Machine IP: $dockerIp" -ForegroundColor Green
#    } else {
#        Write-Host "Failed to get valid IP address. Got: $dockerIp" -ForegroundColor Red
#        Write-Host "Using fallback IP: 192.168.99.100" -ForegroundColor Yellow
#        $dockerIp = "192.168.99.100"
#    }
#} catch {
#    Write-Host "Error getting Docker IP: $_" -ForegroundColor Red
#    Write-Host "Using fallback IP: 192.168.99.100" -ForegroundColor Yellow
#    $dockerIp = "192.168.99.100"
#}
#
## ============================
## Update .env file dynamically
## ============================
#Write-Host "Updating .env file with IP: $dockerIp" -ForegroundColor Cyan
#
#if (Test-Path $envFile) {
#    try {
#        $content = Get-Content $envFile -Raw
#
#        # Update DATABASE_HOST
#        $content = $content -replace 'DATABASE_HOST=.*', "DATABASE_HOST=$dockerIp"
#
#        # Update REDIS_HOST
#        $content = $content -replace 'REDIS_HOST=.*', "REDIS_HOST=$dockerIp"
#
#        # Write back to file
#        $content | Set-Content $envFile -NoNewline
#
#        Write-Host "Successfully updated .env file" -ForegroundColor Green
#        Write-Host "  - DATABASE_HOST=$dockerIp" -ForegroundColor Gray
#        Write-Host "  - REDIS_HOST=$dockerIp" -ForegroundColor Gray
#    } catch {
#        Write-Host "Error updating .env file: $_" -ForegroundColor Red
#    }
#} else {
#    Write-Host ".env file not found at: $envFile" -ForegroundColor Red
#    exit 1
#}
#
## ============================
## Navigate to project directory
## ============================
#Set-Location $projectDir
#
## ============================
## Start Docker Compose
## ============================
#Write-Host ""
#Write-Host "Starting Docker Compose services..." -ForegroundColor Cyan
#
#try {
#    docker-compose -f docker-compose.yml up -d
#
#    if ($LASTEXITCODE -eq 0) {
#        Write-Host ""
#        Write-Host "=====================================" -ForegroundColor Green
#        Write-Host "  Docker services started successfully!" -ForegroundColor Green
#        Write-Host "=====================================" -ForegroundColor Green
#        Write-Host ""
#        Write-Host "Service Endpoints:" -ForegroundColor Cyan
#        Write-Host "  Postgres: ${dockerIp}:5433" -ForegroundColor White
#        Write-Host "  Redis:    ${dockerIp}:6379" -ForegroundColor White
#        Write-Host ""
#        Write-Host "Your .env file has been updated with:" -ForegroundColor Yellow
#        Write-Host "  DATABASE_HOST=${dockerIp}" -ForegroundColor Gray
#        Write-Host "  REDIS_HOST=${dockerIp}" -ForegroundColor Gray
#        Write-Host ""
#        Write-Host "You can now run your Spring Boot application." -ForegroundColor Cyan
#        Write-Host ""
#    } else {
#        Write-Host "Docker Compose encountered issues (exit code: $LASTEXITCODE)" -ForegroundColor Yellow
#    }
#} catch {
#    Write-Host "Error starting Docker Compose: $_" -ForegroundColor Red
#    exit 1
#}
#
## ============================
## Verify containers are running
## ============================
#Write-Host "Verifying containers..." -ForegroundColor Cyan
#docker ps --filter "name=todo-" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
