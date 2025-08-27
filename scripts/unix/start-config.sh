#!/bin/bash
# Script para iniciar config-server con variables de entorno
# Uso: ./start-config.sh

echo "🚀 === Iniciando Config Server con variables de entorno ==="

# Cargar variables de entorno
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$DIR/load-env.sh"

if [ $? -ne 0 ]; then
    echo "❌ Error cargando variables de entorno"
    exit 1
fi

echo ""
echo "🏗️ Iniciando config-server..."
cd ../../config-server

# Detectar SO para usar el wrapper correcto
if [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "win32" ]]; then
    # Windows con Git Bash o similar
    echo "🪟 Detectado entorno Windows"
    ../mvnw.cmd spring-boot:run
else
    # Linux/Mac
    echo "🐧 Detectado entorno Unix/Linux"
    chmod +x ../mvnw
    ../mvnw spring-boot:run
fi

cd ../..