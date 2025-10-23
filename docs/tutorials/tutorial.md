# Smart Garden Backend Tutorial

Welcome to the Smart Garden Backend tutorial! This guide will walk you through setting up and using the Smart Garden IoT system, from initial setup to configuring automated irrigation.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Managing Gardens](#managing-gardens)
- [Registering IoT Devices](#registering-iot-devices)
- [Setting Up Automated Irrigation](#setting-up-automated-irrigation)
- [Monitoring Your Garden](#monitoring-your-garden)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before starting this tutorial, ensure you have:

- Java 21 or higher installed
- Git (for cloning the repository)
- A REST client (curl, Postman, or similar)
- Basic understanding of REST APIs and JSON

---

## Getting Started

### 1. Clone and Run the Application

```bash
# Clone the repository
git clone https://github.com/yourusername/smart-garden-backend.git
cd smart-garden-backend

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### 2. Verify the Application is Running

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

You should see the Swagger UI with all available API endpoints.

### 3. Default Admin Account

The application comes with a pre-configured admin account:
- **Username:** `admin`
- **Password:** `admin123`

---

## Authentication

### Understanding Authentication

The Smart Garden API uses **JWT (JSON Web Token)** authentication for users. Here's how it works:

1. You send your username and password to `/api/v1/auth/login`
2. The server validates your credentials
3. If valid, you receive a JWT token
4. Include this token in all subsequent requests

### Login Example

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNjEyMzQ1NiwiZXhwIjoxNzA2MTMwNjU2fQ.xyz..."
}
```

**Save this token!** You'll need it for all subsequent requests.

### Using the Token

Include the token in the `Authorization` header with the `Bearer` prefix:

```bash
curl -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  http://localhost:8080/api/v1/gardens
```

**Token Expiration:** Tokens are valid for 2 hours by default. After expiration, you'll need to log in again.

---

## Managing Gardens

### Create Your First Garden

Let's create a garden for your backyard:

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/gardens \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backyard Garden",
    "description": "Vegetable garden with tomatoes and peppers",
    "location": "Backyard, near the fence"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Backyard Garden",
  "description": "Vegetable garden with tomatoes and peppers",
  "location": "Backyard, near the fence"
}
```

**Note the `id` field** - you'll need this to reference the garden in other API calls.

### List All Gardens

```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/v1/gardens
```

### Get Garden Details

```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/v1/gardens/1
```

---

## Registering IoT Devices

### Why Register Devices?

Each IoT device (Arduino, ESP32, Raspberry Pi, etc.) needs to be registered to:
- Authenticate with the API
- Associate with a specific garden
- Send sensor data and receive pump commands

### Register a Device

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/devices \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backyard Sensor Node 1",
    "gardenId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Backyard Sensor Node 1",
  "gardenId": 1,
  "apiKey": "d4f3e8b2-1a9c-4d7e-8f3b-2c1d4e5f6a7b"
}
```

**IMPORTANT:** Save the `apiKey`! This is the only time you'll see it. Your IoT device will use this key to authenticate.

### Device Authentication

IoT devices authenticate using the `X-DEVICE-KEY` header (not JWT):

```bash
curl -X POST http://localhost:8080/api/v1/devices/1/data \
  -H "X-DEVICE-KEY: d4f3e8b2-1a9c-4d7e-8f3b-2c1d4e5f6a7b" \
  -H "Content-Type: application/json" \
  -d '{
    "sensorType": "SOIL_MOISTURE",
    "value": 45.5
  }'
```

---

## Setting Up Automated Irrigation

This is where the magic happens! The system can automatically turn on your garden pump when soil moisture drops below a threshold.

### How It Works

1. **You set a threshold** (e.g., "turn on pump when soil moisture < 30%")
2. **Device sends sensor data** every few seconds
3. **System checks threshold** - Is moisture below 30%?
4. **If yes, create pump command** automatically
5. **Device polls for commands** and receives the "START_PUMP" command
6. **Device activates pump** and sends acknowledgment
7. **System logs the activity**

### Step 1: Configure Threshold

```bash
curl -X PUT http://localhost:8080/api/v1/thresholds/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sensorType": "SOIL_MOISTURE",
    "comparator": "LESS_THAN",
    "value": 30.0
  }'
```

This says: "When soil moisture is **less than 30%**, start the pump automatically."

### Step 2: Device Sends Sensor Data

Your IoT device should periodically send sensor readings:

```bash
curl -X POST http://localhost:8080/api/v1/devices/1/data \
  -H "X-DEVICE-KEY: YOUR_DEVICE_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "sensorType": "SOIL_MOISTURE",
    "value": 25.0
  }'
```

**What happens:**
- Moisture is 25% (below the 30% threshold)
- System automatically creates a "START_PUMP" command
- Command is stored in the database, waiting for the device

### Step 3: Device Polls for Commands

Your device should poll every 5-10 seconds:

```bash
curl -H "X-DEVICE-KEY: YOUR_DEVICE_API_KEY" \
  http://localhost:8080/api/v1/devices/1/commands
```

**Response:**
```json
[
  {
    "id": 1,
    "action": "START",
    "createdAt": "2025-01-15T10:30:00Z"
  }
]
```

### Step 4: Device Executes Command

When your device receives a command:
1. **Turn on the pump**
2. **Acknowledge the command** so it won't be sent again:

```bash
curl -X POST http://localhost:8080/api/v1/devices/1/commands/1/ack \
  -H "X-DEVICE-KEY: YOUR_DEVICE_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "success": true
  }'
```

### Manual Pump Control

You can also manually control the pump:

**Start Pump:**
```bash
curl -X POST http://localhost:8080/api/v1/gardens/1/pump/start \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "durationSeconds": 300
  }'
```

**Stop Pump:**
```bash
curl -X POST http://localhost:8080/api/v1/gardens/1/pump/stop \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Monitoring Your Garden

### View Sensor History

Query sensor data for a specific time range:

```bash
curl "http://localhost:8080/api/v1/gardens/1/sensor-data?from=2025-01-15T00:00:00Z&to=2025-01-15T23:59:59Z" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
[
  {
    "id": 1,
    "deviceId": 1,
    "sensorType": "SOIL_MOISTURE",
    "value": 25.0,
    "recordedAt": "2025-01-15T10:30:00Z"
  },
  {
    "id": 2,
    "deviceId": 1,
    "sensorType": "SOIL_MOISTURE",
    "value": 45.0,
    "recordedAt": "2025-01-15T11:00:00Z"
  }
]
```

### View Pump Activity Logs

```bash
curl "http://localhost:8080/api/v1/pump/logs?gardenId=1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
[
  {
    "id": 1,
    "gardenId": 1,
    "action": "START",
    "initiatedBy": "SYSTEM",
    "status": "COMPLETED",
    "startedAt": "2025-01-15T10:30:05Z",
    "completedAt": "2025-01-15T10:35:05Z",
    "durationSeconds": 300
  }
]
```

### Check Alerts

View alerts for threshold breaches:

```bash
curl http://localhost:8080/api/v1/alerts \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Troubleshooting

### Common Issues

#### 1. "401 Unauthorized" Error

**Problem:** Your token is expired or invalid.

**Solution:**
- Log in again to get a new token
- Ensure you're using the `Bearer` prefix in the Authorization header

#### 2. Device Commands Not Appearing

**Problem:** Device polls but receives no commands.

**Possible causes:**
- Threshold not configured
- Sensor value doesn't breach the threshold
- Command already acknowledged

**Solution:**
- Check threshold configuration: `GET /api/v1/thresholds/{gardenId}`
- Verify sensor data is being received
- Check pump logs to see if command was already executed

#### 3. "403 Forbidden" Error

**Problem:** Your user role doesn't have permission for this endpoint.

**Solution:**
- Check if the endpoint requires ADMIN role
- Log in with an admin account

#### 4. Device Authentication Failed

**Problem:** "Invalid API key" error.

**Solution:**
- Verify you're using the correct `X-DEVICE-KEY` header (not `Authorization`)
- Check that the API key matches the one provided during device registration
- Ensure the device ID in the URL matches the registered device

---

## Next Steps

Now that you've completed this tutorial, you can:

1. **Create additional users** - Add USER role accounts for limited access
2. **Add more gardens** - Manage multiple garden areas
3. **Register multiple devices** - Add more sensors and pumps
4. **Set advanced thresholds** - Configure temperature or humidity thresholds
5. **Build a dashboard** - Create a frontend to visualize sensor data
6. **Integrate with mobile apps** - Use the API from mobile devices

### API Documentation

For complete API reference, visit:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Javadoc:** Generated at `build/docs/javadoc/index.html`

### Further Reading

- [README.md](../../README.md) - Project overview and setup
- [STATIC_ANALYSIS.md](../../STATIC_ANALYSIS.md) - Code quality guidelines
- [TESTING_GUIDE.md](../../TESTING_GUIDE.md) - Testing documentation

---

## Support

If you encounter any issues not covered in this tutorial:
1. Check the application logs
2. Review the API documentation in Swagger
3. Open an issue on GitHub

Happy gardening!

