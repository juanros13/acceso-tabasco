#!/bin/bash
# Script para cargar variables de entorno desde .env
# Uso: source ./load-env.sh

echo "🔧 Cargando variables de entorno desde .env..."

if [ ! -f "../../.env" ]; then
    echo "❌ Error: No se encontró el archivo .env"
    echo "💡 Copia .env.example como .env y configura tus valores"
    return 1 2>/dev/null || exit 1
fi

# Cargar variables ignorando comentarios y líneas vacías
while IFS='=' read -r key value; do
    # Ignorar comentarios y líneas vacías
    if [[ $key =~ ^[[:space:]]*# ]] || [[ -z $key ]]; then
        continue
    fi
    
    # Remover espacios en blanco
    key=$(echo "$key" | xargs)
    value=$(echo "$value" | xargs)
    
    # Remover comillas si las hay
    value=$(echo "$value" | sed 's/^["'"'"']//g' | sed 's/["'"'"']$//g')
    
    # Exportar variable
    export "$key"="$value"
    echo "  ✅ $key = $value"
    
done < ../../.env

echo "🎉 Variables de entorno cargadas exitosamente!"