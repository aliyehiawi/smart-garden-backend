# Smart Garden Backend

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)

A RESTful backend API for IoT-based smart garden management, featuring automated irrigation, real-time sensor monitoring, and device management.

## Overview

Smart Garden Backend is a Spring Boot application that enables automated garden monitoring and irrigation control through IoT devices. The system supports multiple gardens, devices, and users with role-based access control, JWT authentication, and real-time data processing.

**Key Capabilities:**

- Automated irrigation based on configurable thresholds
- Real-time sensor data collection and monitoring
- Multi-garden and multi-device management
- User authentication with JWT tokens
- Device authentication with API keys
- Historical data tracking and alerts

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database &amp; Configuration](#database--configuration)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [License](#license)

---

## Features

- **User Management**

  - JWT-based authentication
  - Role-based access control (ADMIN/USER)
  - Secure password encryption
- **Garden Management**

  - Create and manage multiple gardens
  - Assign users to gardens
  - Configure location and descriptions
- **Device Management**

  - Register IoT devices with unique API keys
  - Device authentication and authorization
  - Support for multiple devices per garden
- **Sensor Monitoring**

  - Real-time sensor data collection
  - Support for various sensor types (soil moisture, temperature, humidity)
  - Historical data storage and querying
  - Time-range based data retrieval
- **Automated Irrigation**

  - Threshold-based automatic watering
  - Manual pump control
  - Pump activity logging
  - Command acknowledgment system
- **Alerts & Notifications**

  - Configurable threshold alerts
  - User-specific notifications
  - Alert history tracking
- **API Documentation**

  - Interactive Swagger UI
  - OpenAPI 3.0 specification
  - Comprehensive endpoint documentation

---

## Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/smart-garden-backend.git
cd smart-garden-backend

# Run the application
./gradlew bootRun

# Access the API
# Swagger UI: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
```

**Default credentials:**

- Username: `admin`
- Password: `admin123`

---

## Prerequisites

Before building this project, ensure you have:

- **Java 21** (JDK 21 or higher)
  - Check version: `java -version`
- **No Gradle installation required** - Project includes Gradle Wrapper (`gradlew`)
- **Git** (for cloning the repository)

---

## Build Tool: Gradle

This project uses **Gradle** as its build automation tool. Gradle is chosen for:

- **Dependency Management**: Automatically downloads and manages external libraries from Maven Central
- **Compilation Automation**: Compiles Java source code with annotation processing (Lombok, MapStruct)
- **Version Management**: Manages project version (0.0.1-SNAPSHOT) and dependency versions
- **Packaging**: Creates executable JAR files with embedded Tomcat server
- **Testing Automation**: Runs JUnit 5 tests with a single command
- **Custom Tasks**: Supports custom build tasks for project automation

The **Gradle Wrapper** (`gradlew` / `gradlew.bat`) is included, so you don't need to install Gradle manually. It ensures all use the same Gradle version for consistent builds.

---

## Building the Project

### Clean and Build

Compiles source code, processes annotations, runs tests, and packages the application:

```bash
# Linux/Mac
./gradlew clean build

# Windows
gradlew.bat clean build
```

**What this does:**

- Downloads all dependencies from Maven Central
- Compiles Java 21 source code
- Processes Lombok and MapStruct annotations
- Runs all tests
- Creates executable JAR in `build/libs/`

### Build without Tests

For faster builds during development:

```bash
./gradlew clean build -PskipTests
```

---

## Testing

### Run Unit Tests

Execute the full test suite with JUnit 5 and Mockito:

```bash
./gradlew test
```

Test reports: `build/reports/tests/test/index.html`

### Run Code Quality Checks

Static analysis with Checkstyle and SpotBugs:

```bash
./gradlew codeQuality
```

See [STATIC_ANALYSIS.md](STATIC_ANALYSIS.md) for configuration details.

### API Testing

For comprehensive API testing with Postman:

1. Import `Smart-Garden-API.postman_collection.json`
2. Follow the [Testing Guide](TESTING_GUIDE.md) for step-by-step instructions

---

## Packaging

### Create Executable JAR

Build a standalone executable JAR with embedded Tomcat:

```bash
./gradlew bootJar
```

**Output:** `build/libs/smart-garden-backend-0.0.1-SNAPSHOT.jar`

### Run the Packaged JAR

Execute the built JAR directly:

```bash
java -jar build/libs/smart-garden-backend-0.0.1-SNAPSHOT.jar
```

The application starts on `http://localhost:8080`

---

## Running the Application

### Option 1: Run with Gradle (Development)

Runs the application directly without creating a JAR:

```bash
./gradlew bootRun
```

### Option 2: Run Packaged JAR (Production-like)

First build, then execute:

```bash
./gradlew bootJar
java -jar build/libs/smart-garden-backend-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

---

## API Documentation

### Swagger UI

Interactive API documentation is available at:

**URL:** http://localhost:8080/swagger-ui.html

The Swagger UI provides:

- Complete endpoint documentation
- Request/response schemas
- Interactive API testing
- Authentication support

### OpenAPI Specification

Raw OpenAPI 3.0 specification available at:

**URL:** http://localhost:8080/v3/api-docs

---

## Dependency Management

All dependencies are managed in `build.gradle` and automatically resolved from Maven Central:

### Core Dependencies

- **Spring Boot 3.5.6** - Web, JPA, Security, Validation
- **Lombok** - Reduces boilerplate code (getters, setters, constructors)
- **MapStruct 1.5.5** - Type-safe DTO mapping
- **H2 Database** - In-memory database for development
- **JJWT 0.11.5** - JWT token authentication
- **Springdoc OpenAPI 2.7.0** - API documentation and Swagger UI

### View All Dependencies

List all project dependencies with versions:

```bash
./gradlew listDependencies
```

### View Project Information

Display project metadata:

```bash
./gradlew projectInfo
```

---

## Useful Gradle Commands

| Command                    | Description              |
| -------------------------- | ------------------------ |
| `./gradlew tasks`        | List all available tasks |
| `./gradlew build`        | Full build with tests    |
| `./gradlew clean`        | Remove build artifacts   |
| `./gradlew test`         | Run tests only           |
| `./gradlew bootRun`      | Run application          |
| `./gradlew bootJar`      | Create executable JAR    |
| `./gradlew dependencies` | Show dependency tree     |

---

## Database & Configuration

Default admin

- username: admin
- password: admin123

H2 console

- URL: `http://localhost:8080/h2-console`
- JDBC: `jdbc:h2:mem:smartgarden`
- User: `sa`
- Password: (empty)

Swagger UI

Interactive API documentation available at:

- URL: `http://localhost:8080/swagger-ui.html`

Environment

Configuration in `application.properties`:

- `jwt.secret` - Secure 256-bit secret for signing JWT tokens
- `jwt.expiration.minutes=120` - Tokens expire after 2 hours
- `springdoc.api-docs.enabled=true` - Swagger API docs enabled
- `springdoc.swagger-ui.enabled=true` - Swagger UI enabled

### Main API Endpoints

**Authentication** (Public)

| Method | Endpoint               | Description                               |
| ------ | ---------------------- | ----------------------------------------- |
| POST   | `/api/v1/auth/login` | Login with credentials, returns JWT token |

**User Management** (ADMIN only)

| Method | Endpoint          | Description     |
| ------ | ----------------- | --------------- |
| POST   | `/api/v1/users` | Create new user |
| GET    | `/api/v1/users` | List all users  |

**Garden Management**

| Method | Endpoint                 | Description        | Role       |
| ------ | ------------------------ | ------------------ | ---------- |
| POST   | `/api/v1/gardens`      | Create garden      | ADMIN      |
| GET    | `/api/v1/gardens`      | List gardens       | ADMIN/USER |
| GET    | `/api/v1/gardens/{id}` | Get garden details | ADMIN/USER |

**Device Management** (ADMIN only)

| Method | Endpoint            | Description             |
| ------ | ------------------- | ----------------------- |
| POST   | `/api/v1/devices` | Register new IoT device |

**Irrigation Control** (ADMIN only)

| Method | Endpoint                            | Description                   |
| ------ | ----------------------------------- | ----------------------------- |
| PUT    | `/api/v1/thresholds/{gardenId}`   | Set/update watering threshold |
| POST   | `/api/v1/gardens/{id}/pump/start` | Manually start pump           |
| POST   | `/api/v1/gardens/{id}/pump/stop`  | Manually stop pump            |
| GET    | `/api/v1/pump/logs`               | View pump activity logs       |

**Monitoring**

| Method | Endpoint                             | Description          | Role       |
| ------ | ------------------------------------ | -------------------- | ---------- |
| GET    | `/api/v1/gardens/{id}/sensor-data` | Query sensor history | ADMIN/USER |
| GET    | `/api/v1/alerts`                   | Get user alerts      | USER       |

**IoT Device Endpoints** (API Key Authentication via `X-DEVICE-KEY` header)

| Method | Endpoint                                                | Description                   |
| ------ | ------------------------------------------------------- | ----------------------------- |
| POST   | `/api/v1/devices/{deviceId}/data`                     | Send sensor readings          |
| GET    | `/api/v1/devices/{deviceId}/commands`                 | Poll for pump commands        |
| POST   | `/api/v1/devices/{deviceId}/commands/{commandId}/ack` | Acknowledge command execution |

### Learn More

**For detailed usage examples**, see the [Tutorial](docs/tutorials/tutorial.md)

**For complete API reference**, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)

**For testing with Postman**, see the [Testing Guide](TESTING_GUIDE.md)

---

## Architecture

### Authentication & Security

- **User Authentication:** JWT token-based (Bearer token in Authorization header)
- **Device Authentication:** API key-based (X-DEVICE-KEY header)
- **Password Security:** BCrypt encryption
- **Role-Based Access Control:** ADMIN and USER roles

### Communication Pattern

- **HTTP/REST API:** Pure RESTful architecture
- **Device Polling:** Devices poll for commands every 5-10 seconds
- **Command Storage:** Commands persisted in database until acknowledged
- **No Message Broker:** Direct HTTP communication (no MQTT/WebSocket required)

### Automated Irrigation Flow

1. **Threshold Configuration:** Admin sets moisture threshold (e.g., < 30%)
2. **Data Collection:** Device sends sensor reading to API
3. **Threshold Check:** System detects moisture below threshold
4. **Command Creation:** Pump start command created automatically
5. **Device Polling:** Device retrieves command on next poll
6. **Execution:** Device activates pump and sends acknowledgment
7. **Logging:** System records pump activity with duration and status

### Data Flow

```
IoT Device → POST sensor data → API → Store in DB → Check thresholds
                                  ↓
                           Create command (if needed)
                                  ↓
IoT Device ← GET commands ← API ← Retrieve pending commands
```

---

## Technologies

### Backend Framework

- **Spring Boot 3.5.6** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **Spring Validation** - Request validation

### Database

- **H2 Database** - In-memory database (development)
- **JPA/Hibernate** - ORM framework

### Security & Authentication

- **JWT (JJWT 0.11.5)** - JSON Web Token implementation
- **BCrypt** - Password hashing

### Code Quality & Utilities

- **Lombok** - Reduces boilerplate code
- **MapStruct 1.5.5** - Type-safe DTO mapping
- **Checkstyle** - Code style enforcement
- **SpotBugs** - Static bug detection

### API Documentation

- **Springdoc OpenAPI 2.7.0** - OpenAPI 3.0 specification
- **Swagger UI** - Interactive API documentation

### Build & Testing

- **Gradle 8.14.3** - Build automation
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework

### Language

- **Java 21** - LTS version with modern features

---

## Documentation

This project includes comprehensive documentation:

| Document | Purpose | Audience |
|----------|---------|----------|
| [README.md](README.md) | Project overview and quick start | Everyone |
| [Tutorial](docs/tutorials/tutorial.md) | Step-by-step learning guide | New users |
| [Testing Guide](TESTING_GUIDE.md) | Postman collection usage | Testers |
| [Static Analysis](STATIC_ANALYSIS.md) | Code quality tools setup | Developers |
| [Swagger UI](http://localhost:8080/swagger-ui.html) | Complete API reference | API consumers |
| [Javadoc](build/docs/javadoc/index.html) | Code documentation | Developers |

---

## Contributing

Contributions are welcome! This project was created for educational purposes.

### Quick Start for Contributors

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Run quality checks: `./gradlew build codeQuality`
5. Commit with conventional commits (see below)
6. Push and open a Pull Request

### Code Quality Requirements

All contributions must pass:

- **Checkstyle** - Code style verification  
- **SpotBugs** - Static bug detection  
- **Unit Tests** - Test coverage for new features  

```bash
# Run all quality checks
./gradlew codeQuality test
```

See [STATIC_ANALYSIS.md](STATIC_ANALYSIS.md) for detailed guidelines.

### Commit Message Format

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>: <description>

[optional body]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `test`: Adding/updating tests
- `refactor`: Code refactoring
- `chore`: Maintenance tasks

**Examples:**
```
feat: add automated irrigation thresholds
fix: resolve JWT token expiration issue
docs: update API tutorial with examples
test: add unit tests for UserService
```

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
