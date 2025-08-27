# Discovery Service (Eureka Server) Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar wrapper de Maven
COPY mvnw* ./
COPY .mvn .mvn

# Copiar POM y descargar dependencias
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8761

# Comando de inicio
ENTRYPOINT ["java", "-jar", "target/discovery-service-0.0.1-SNAPSHOT.jar"]