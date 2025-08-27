# Config Server Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar wrapper de Maven
COPY mvnw* ./
COPY .mvn .mvn

# Copiar POM y descargar dependencias
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente y configuraciones
COPY src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8888

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=dev
ENV SPRING_CLOUD_CONFIG_SERVER_NATIVE_SEARCHLOCATIONS=classpath:/config-repo

# Comando de inicio
ENTRYPOINT ["java", "-jar", "target/config-server-0.0.1-SNAPSHOT.jar"]