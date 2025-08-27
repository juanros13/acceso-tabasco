# Script para iniciar config-server con variables de entorno
# Uso: .\start-config.ps1

Write-Host "=== Iniciando Config Server con variables de entorno ===" -ForegroundColor Cyan

# Cargar variables de entorno
& "$PSScriptRoot\load-env.ps1"

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

Write-Host "`nIniciando config-server..." -ForegroundColor Green
Set-Location "../../config-server"

try {
    & "../mvnw.cmd" spring-boot:run
} finally {
    Set-Location "../.."
}