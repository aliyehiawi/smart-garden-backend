# Smart Garden Backend

IoT Smart Garden Backend - Automated irrigation system with sensor monitoring.

Spring Boot 3.5.6, Java 21 backend for Smart Garden IoT system.

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

### Run All Tests

Execute the full test suite with JUnit 5:

```bash
./gradlew test
```

### View Test Reports

After running tests, open the HTML report:

- Location: `build/reports/tests/test/index.html`

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

Main endpoints

**Authentication**

- POST `/api/v1/auth/login` - Login with username/password, returns JWT token

**User Management** (ADMIN only)

- POST `/api/v1/users` - Create new user
- GET `/api/v1/users` - List all users

**Garden Management**

- POST `/api/v1/gardens` - Create garden (ADMIN)
- GET `/api/v1/gardens` - List gardens (ADMIN/USER)
- GET `/api/v1/gardens/{id}` - Get garden details (ADMIN/USER)

**Device Management**

- POST `/api/v1/devices` - Register new IoT device (ADMIN)

**Threshold Configuration**

- PUT `/api/v1/thresholds/{gardenId}` - Set/update watering threshold (ADMIN)

**Pump Control**

- POST `/api/v1/gardens/{id}/pump/start` - Manually start pump (ADMIN)
- POST `/api/v1/gardens/{id}/pump/stop` - Manually stop pump (ADMIN)
- GET `/api/v1/pump/logs?gardenId=` - View pump activity logs (ADMIN)

**Monitoring**

- GET `/api/v1/gardens/{id}/sensor-data?from=&to=` - Query sensor history
- GET `/api/v1/alerts` - Get user alerts (USER)

IoT Device Endpoints (API Key Authentication)

Devices use `X-DEVICE-KEY` header for authentication:

- POST `/api/v1/devices/{deviceId}/data` - Send sensor readings
- GET `/api/v1/devices/{deviceId}/commands` - Poll for pump commands
- POST `/api/v1/devices/{deviceId}/commands/{commandId}/ack` - Acknowledge command execution

API Examples

**1. Login and get JWT token:**

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**2. Create a garden:**

```bash
TOKEN="your-jwt-token-here"
curl -X POST http://localhost:8080/api/v1/gardens \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Garden A","description":"My tomato garden","location":"Backyard"}'
```

**3. Register an IoT device:**

```bash
curl -X POST http://localhost:8080/api/v1/devices \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"deviceId":"ESP32-001","gardenId":1}'
# Response includes the generated API key for the device
```

**4. Device sends sensor data:**

```bash
curl -X POST http://localhost:8080/api/v1/devices/ESP32-001/data \
  -H "X-DEVICE-KEY: your-device-api-key" \
  -H "Content-Type: application/json" \
  -d '{"sensorType":"SOIL_MOISTURE","value":23.4}'
```

**5. Device polls for commands:**

```bash
curl -X GET http://localhost:8080/api/v1/devices/ESP32-001/commands \
  -H "X-DEVICE-KEY: your-device-api-key"
```

**6. Manually start pump:**

```bash
curl -X POST http://localhost:8080/api/v1/gardens/1/pump/start \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"durationSeconds":60}'
```

Architecture

**Authentication:**

- Users: JWT token-based authentication (Bearer token)
- IoT Devices: API key authentication (X-DEVICE-KEY header)

**Device Communication:**

- Devices poll for commands every 5-10 seconds (HTTP polling)
- Commands stored in database until acknowledged
- No MQTT broker required - pure HTTP/REST architecture

**Auto-Watering:**

- Configure thresholds for each garden (e.g., soil moisture < 30%)
- When threshold is breached, system automatically creates pump command
- Device receives command on next poll and executes it

Technologies

- Spring Boot 3.5.6
- Spring Security (JWT + API Key)
- Spring Data JPA
- H2 Database (in-memory for development)
- MapStruct (DTO mapping)
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Java 21
