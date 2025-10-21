Smart Garden Backend

Spring Boot 3.5.6, Java 21 backend for Smart Garden IoT system.

Build

```bash
./gradlew clean build
```

Run

```bash
./gradlew bootRun
```

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


