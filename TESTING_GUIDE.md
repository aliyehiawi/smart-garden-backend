# Smart Garden API - Postman Testing Guide

> **Purpose:** This guide provides step-by-step instructions for testing the Smart Garden API using the included Postman collection. Perfect for manual API testing, validation, and learning how the system works.

**For other testing approaches:**
- **Unit/Integration Tests:** Run `./gradlew test`
- **Code Quality Checks:** See [STATIC_ANALYSIS.md](STATIC_ANALYSIS.md)
- **API Learning Tutorial:** See [docs/tutorials/tutorial.md](docs/tutorials/tutorial.md)

---

## Quick Start

### 1. Import Postman Collection

1. Open Postman
2. Click **Import** → Select `Smart-Garden-API.postman_collection.json`
3. The collection will be imported with all requests and environment variables

### 2. Start the Application

```bash
./gradlew bootRun
```

Application runs at: `http://localhost:8080`

### 3. Default Credentials

- **Username**: `admin`
- **Password**: `admin123`

---

## Complete Testing Flow (Step-by-Step)

### **Step 1: Login**
**Request**: `1. Authentication → Login (Admin)`

**What it does**:
- Authenticates with admin credentials
- Automatically saves JWT token to collection variable
- Token is auto-used for all subsequent requests

**Expected Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### **Step 2: Create a User** (Optional)
**Request**: `2. User Management → Create User`

**What it tests**:
- User creation endpoint
- `@ValidEnum` annotation for role validation

**Expected Response** (200 OK):
```json
{
  "id": 2,
  "username": "testuser",
  "role": "USER"
}
```

**Test Invalid Role**:
- Run: `Test Invalid Role (Should return 400)`
- Expected: `400 Bad Request` with error message listing valid roles: `ADMIN, USER`

**Test Duplicate Username**:
- Run: `Test Duplicate Username (Should return 409)`
- Expected: `409 Conflict` with message: `"Username already exists"`

---

### **Step 3: List Users**
**Request**: `2. User Management → List All Users`

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "testuser",
    "role": "USER"
  }
]
```

---

### **Step 4: Create a Garden**
**Request**: `3. Garden Management → Create Garden`

**What it does**:
- Creates a new garden
- Automatically saves `garden_id` to collection variable

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "name": "My First Garden",
  "description": "A beautiful vegetable garden",
  "location": "Backyard"
}
```

---

### **Step 5: List Gardens**
**Request**: `3. Garden Management → List My Gardens`

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "My First Garden",
    "description": "A beautiful vegetable garden",
    "location": "Backyard"
  }
]
```

---

### **Step 6: Register a Device**
**Request**: `4. Device Management → Register Device`

**What it does**:
- Registers ESP32/IoT device
- Generates API key for device authentication
- Automatically saves `device_id` and `device_api_key` to collection variables

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "deviceId": "ESP32-001",
  "apiKey": "a3f5b2c8d9e1f4a6b7c8d9e0f1a2b3c4",
  "gardenId": 1,
  "enabled": true
}
```

**Important:** Save the `apiKey` - it's needed for device requests!

---

### **Step 7: Set Thresholds**
**Request**: `5. Threshold Management → Create/Update Threshold - Soil Moisture`

**What it does**:
- Sets auto-watering rules
- When soil moisture < 30%, automatically start pump for 30 seconds

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "gardenId": 1,
  "sensorType": "SOIL_MOISTURE",
  "thresholdValue": 30.0,
  "comparator": "BELOW",
  "autoWaterEnabled": true,
  "pumpMaxSeconds": 30
}
```

**Also test**:
- `Create/Update Threshold - Temperature`
- `Test Invalid Sensor Type (Should return 400)` → Validates enum

---

### **Step 8: Post Sensor Data (Device)**
**Request**: `6. Sensor Data → Post Sensor Reading - Soil Moisture Low`

**What it does**:
- Simulates device sending sensor data
- Uses `X-API-KEY` header (not JWT)
- If value < 30% → **Automatically triggers pump!**

**Expected Response** (200 OK):
```json
{}
```

**Check Console Logs**:
```
INFO - auto-watering-trigger gardenId=1 type=SOIL_MOISTURE value=25.5
INFO - pump-start gardenId=1 durationSeconds=30 initiatedBy=AUTO
```

**Also test**:
- Post temperature reading
- Post humidity reading

---

### **Step 9: Get Pending Commands (Device)**
**Request**: `4. Device Management → Get Pending Commands`

**What it does**:
- Device polls for commands (pump start/stop)
- Returns commands created by auto-watering or manual control

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "action": "START",
    "durationSeconds": 30,
    "acknowledged": false,
    "createdAt": "2025-10-21T10:22:45"
  }
]
```

---

### **Step 10: Manual Pump Control**
**Request**: `7. Pump Control → Start Pump (Manual)`

**What it does**:
- Manually start pump for 15 seconds
- Creates command for device to execute

**Expected Response** (200 OK):
```json
{}
```

**Then Stop**:
- Run: `Stop Pump`

---

### **Step 11: View Pump Logs**
**Request**: `7. Pump Control → Get Pump Logs`

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "gardenId": 1,
    "deviceId": "broadcast",
    "action": "START",
    "startedAt": "2025-10-21T10:22:45",
    "durationSeconds": 30,
    "initiatedBy": "AUTO",
    "status": "SUCCESS"
  },
  {
    "id": 2,
    "gardenId": 1,
    "deviceId": "broadcast",
    "action": "START",
    "startedAt": "2025-10-21T10:25:12",
    "durationSeconds": 15,
    "initiatedBy": "MANUAL",
    "status": "SUCCESS"
  }
]
```

---

### **Step 12: View Sensor History**
**Request**: `6. Sensor Data → Get Sensor History`

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "deviceId": "ESP32-001",
    "sensorType": "SOIL_MOISTURE",
    "value": 25.5,
    "timestamp": "2025-10-21T10:22:30"
  },
  {
    "id": 2,
    "deviceId": "ESP32-001",
    "sensorType": "TEMP",
    "value": 28.3,
    "timestamp": "2025-10-21T10:23:15"
  }
]
```

---

### **Step 13: Check Alerts**
**Request**: `8. Alerts → Get Alerts for Garden`

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "gardenId": 1,
    "message": "Soil moisture below threshold (25.5% < 30.0%)",
    "severity": "WARNING",
    "read": false,
    "createdAt": "2025-10-21T10:22:30"
  }
]
```

---

## Testing Exception Handling

### Test 1: Invalid Enum Values (400 Bad Request)
- **Request**: `2. User Management → Test Invalid Role`
- **Expected**: `400` with message listing valid enum values
- **Tests**: `@ValidEnum` validation

### Test 2: Duplicate Resource (409 Conflict)
- **Request**: `2. User Management → Test Duplicate Username`
- **Expected**: `409` with message `"Username already exists"`
- **Tests**: `ConflictException`

### Test 3: Resource Not Found (404 Not Found)
- **Try**: GET `/api/v1/gardens/999`
- **Expected**: `404` with message `"Garden not found"`
- **Tests**: `NotFoundException`

### Test 4: Unauthorized (401)
- **Try**: Any request without `Authorization` header
- **Expected**: `401 Unauthorized`

### Test 5: Forbidden (403)
- **Login as regular user**, then try to create another user
- **Expected**: `403 Forbidden` (only admins can create users)

---

## Database Access (H2 Console)

**URL**: http://localhost:8080/h2-console

**Credentials**:
- **JDBC URL**: `jdbc:h2:mem:smartgarden`
- **Username**: `sa`
- **Password**: *(leave empty)*

**Useful Queries**:
```sql
-- View all users
SELECT * FROM "USER";

-- View all gardens
SELECT * FROM GARDEN;

-- View all devices
SELECT * FROM DEVICE;

-- View sensor data
SELECT * FROM SENSOR_DATA ORDER BY TIMESTAMP DESC;

-- View pump logs
SELECT * FROM PUMP_LOG ORDER BY STARTED_AT DESC;

-- View thresholds
SELECT * FROM THRESHOLD;
```

---

## Authentication Types

### 1. JWT (User Endpoints)
- **Header**: `Authorization: Bearer <token>`
- **Used for**: User, Garden, Pump, Threshold, Alert endpoints
- **Get token from**: Login endpoint

### 2. API Key (Device Endpoints)
- **Header**: `X-API-KEY: <device_api_key>`
- **Used for**: Sensor data posting, command retrieval
- **Get key from**: Device registration

---

## Available Enum Values

### UserRole
- `ADMIN`
- `USER`

### SensorType
- `SOIL_MOISTURE`
- `TEMP`
- `HUMIDITY`

### ComparatorType
- `BELOW`
- `ABOVE`

### PumpAction
- `START`
- `STOP`

### PumpStatus
- `SUCCESS`
- `FAILED`

### InitiatedBy
- `MANUAL`
- `AUTO`

---

## Key Features to Test

### Custom Validation (`@ValidEnum`)
- Try invalid enum values → Should get 400 with helpful message
- Validates: `role`, `sensorType`, `comparator`, `status`

### Exception Handling
- `400 Bad Request` → Invalid input
- `404 Not Found` → Resource doesn't exist
- `409 Conflict` → Duplicate resource
- `403 Forbidden` → No permission
- `401 Unauthorized` → Not logged in

### Auto-Watering Logic
1. Set threshold (soil moisture < 30%, auto-water ON)
2. Post sensor reading with value 25%
3. Check logs → Should see auto-watering triggered
4. Check pending commands → Should see START command

### Device Authentication
- Device endpoints use API key (not JWT)
- Test with wrong API key → Should get 401

---

## Common Issues

### Issue: "401 Unauthorized"
**Solution**: Make sure you ran the Login request first. JWT token should be auto-saved.

### Issue: "Garden not found"
**Solution**: Run "Create Garden" first and ensure `garden_id` variable is set.

### Issue: "Device not found"
**Solution**: Run "Register Device" first and ensure `device_id` variable is set.

### Issue: Variables not auto-saving
**Solution**: Check the "Tests" tab in each request - scripts should be present.

---

## Testing Sequence Summary

```
1. Login (Admin) → Saves JWT token
2. Create User
3. Create Garden → Saves garden_id
4. Register Device → Saves device_id & api_key
5. Set Threshold (Soil < 30%, auto-water)
6. Post Sensor Data (25%) → Triggers auto-watering
7. Get Pending Commands → See pump START command
8. Get Pump Logs → See AUTO pump log
9. Start Pump (Manual)
10. Get Sensor History
11. View Alerts
```

---

**Happy Testing!** Your Smart Garden API is fully functional with proper exception handling, validation, and authentication!

---

## Related Documentation

- **[Tutorial](docs/tutorials/tutorial.md)** - Learn the API step-by-step with explanations
- **[README](README.md)** - Project overview and setup instructions
- **[Swagger UI](http://localhost:8080/swagger-ui.html)** - Interactive API documentation
- **[Static Analysis Guide](STATIC_ANALYSIS.md)** - Code quality tools and standards

