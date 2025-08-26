# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.5 application built for Spring Cloud Task. The project is named "Acceso-tabasco" and uses Java 17. It's a minimal Spring Boot application designed as a Spring Cloud Task runner.

## Architecture

- **Main Application**: `AccesoTabascoApplication.java` - Standard Spring Boot application entry point
- **Package Structure**: `saf.cgmaig` - All Java code is organized under this base package
- **Framework**: Spring Boot with Spring Cloud Task integration
- **Java Version**: Java 17

## Common Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Package the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing
```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=AccesoTabascoApplicationTests

# Run tests with coverage
./mvnw test jacoco:report
```

### Maven Wrapper
Use the Maven wrapper (`mvnw` on Unix/Linux, `mvnw.cmd` on Windows) instead of a system-wide Maven installation to ensure consistent build environment.

## Configuration

- **Application Properties**: `src/main/resources/application.properties`
- **Application Name**: "Acceso-tabasco" (configured in application.properties)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── saf/cgmaig/
│   │       └── AccesoTabascoApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── saf/cgmaig/
            └── AccesoTabascoApplicationTests.java
```

## Dependencies

- **Spring Boot Starter Parent**: 3.5.5
- **Spring Cloud Task**: For task execution capabilities
- **Spring Boot Starter Test**: For testing framework (JUnit 5)
- **Spring Cloud BOM**: 2025.0.0 for dependency management

## Development Notes

- This is a Spring Cloud Task application, designed for batch processing or one-time task execution
- The application follows standard Spring Boot conventions
- Maven is the build tool with wrapper scripts for cross-platform compatibility