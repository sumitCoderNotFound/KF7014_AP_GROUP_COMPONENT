# API Endpoints Documentation

This document provides a comprehensive list of all API endpoints available in the water quality monitoring system.

## Gateway Service

### Fallback Endpoint

- **Endpoint**: `/fallback`
- **Method**: GET
- **Description**: Returns a fallback message when the water quality microservice is unavailable.
- **Response**: Text message indicating that the water quality microservice is currently not available.
- **Authentication**: Not required

## Monitoring Microservice

### Get Latest Water Reading

- **Endpoint**: `/watermonitoring/records/latest`
- **Method**: GET
- **Description**: Retrieves the latest water reading record from the database based on timestamp.
- **Parameters**: 
  - `Authorization` (Header): Bearer token for authentication
- **Response Codes**:
  - 200: Successfully retrieved the latest record
  - 204: No records found
  - 401: Unauthorized (token expired)
  - 403: Forbidden (invalid or missing token)
  - 500: Internal server error
- **Response Body**: WaterReading object (when successful)
- **Authentication**: Required (JWT)

### Get All Water Quality Records

- **Endpoint**: `/watermonitoring/records`
- **Method**: GET
- **Description**: Retrieves all water quality records from the database.
- **Parameters**: 
  - `Authorization` (Header): Bearer token for authentication
- **Response Codes**:
  - 200: Successfully retrieved the records
  - 204: No records available
  - 401: Unauthorized (token expired)
  - 403: Forbidden (invalid or missing token)
  - 500: Internal server error
- **Response Body**: Array of WaterReading objects (when successful)
- **Authentication**: Required (JWT)

## Water Quality Microservice

### Get Latest Water Quality with Safety Assessment

- **Endpoint**: `/waterquality/records/latestflagged`
- **Method**: GET
- **Description**: Fetches the latest water quality record, calculates total dissolved solids (TDS), and assigns a safety flag based on predefined water quality parameters.
- **Parameters**: 
  - `Authorization` (Header): Bearer token for authentication
- **Response Codes**:
  - 200: Successfully retrieved the water quality data
  - 204: No records found
  - 401: Unauthorized (token expired)
  - 403: Forbidden (invalid or missing token)
  - 500: Internal server error
- **Response Body**: Water quality data with TDS calculation and safety flag (when successful)
- **Authentication**: Required (JWT)

## Authentication

All endpoints (except the fallback endpoint) require authentication using JWT tokens. The token must be provided in the Authorization header with the format: `Bearer <token>`.