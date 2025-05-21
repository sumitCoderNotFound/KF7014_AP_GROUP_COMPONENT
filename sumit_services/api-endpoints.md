# API Endpoints Documentation

This document provides a comprehensive list of all API endpoints available in the Water Quality Monitoring System.

## Authentication Service

These endpoints are whitelisted in the API Gateway and don't require authentication:

- **GET /api/authenticate** - Base authentication endpoint
- **POST /api/authenticate/login** - User login endpoint
- **POST /api/authenticate/register** - User registration endpoint
- **POST /api/authenticate/refresh-token** - Refresh an expired JWT token
- **GET /api/authenticate/validate** - Validate a JWT token

Note: The implementation details of these endpoints are not available in the current repository.

## Monitoring Service

Base path: `/api/water-quality`

All endpoints require authentication via JWT token in the Authorization header.

### Get All Water Quality Records

- **URL:** `/api/water-quality/records`
- **Method:** GET
- **Auth required:** Yes (JWT token in Authorization header)
- **Response:** List of WaterQualityRecord objects
- **Description:** Retrieves all water quality records from the monitoring service.

### Get Latest Water Quality Record

- **URL:** `/api/water-quality/latest`
- **Method:** GET
- **Auth required:** Yes (JWT token in Authorization header)
- **Response:** WaterQualityRecord object
- **Description:** Retrieves the latest water quality record from the monitoring service.

## Quality Check Service

Base path: `/api/quality-check`

All endpoints require authentication via JWT token in the Authorization header.

### Validate Water Quality

- **URL:** `/api/quality-check/validate`
- **Method:** GET
- **Auth required:** Yes (JWT token in Authorization header)
- **Response:** QualityCheckResponse object
- **Description:** Validates water quality by fetching the latest water quality data from the monitoring service and then validating it.

## API Gateway Routes

The API Gateway routes requests to the appropriate services:

- Requests to `/api/water-quality/**` are routed to the monitoring-service at http://localhost:8112
- Requests to `/api/quality-check/**` are routed to the quality-check-service at http://localhost:8113