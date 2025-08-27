# PowerShell script para cargar variables de entorno desde .env
# Uso: .\load-env.ps1

Write-Host "Cargando variables de entorno desde .env..." -ForegroundColor Green

if (Test-Path "../../.env") {
    Get-Content ../../.env | ForEach-Object {
        if ($_ -match "^\s*([^#][^=]*)\s*=\s*(.*)\s*$") {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            
            # Remover comillas si las hay
            $value = $value -replace '^["'']|["'']$'
            
            Set-Item -Path "env:$name" -Value $value
            Write-Host "  $name = $value" -ForegroundColor Yellow
        }
    }
    Write-Host "Variables de entorno cargadas exitosamente!" -ForegroundColor Green
} else {
    Write-Host "Error: No se encontró el archivo .env" -ForegroundColor Red
    Write-Host "Copia .env.example como .env y configura tus valores" -ForegroundColor Red
    exit 1
}