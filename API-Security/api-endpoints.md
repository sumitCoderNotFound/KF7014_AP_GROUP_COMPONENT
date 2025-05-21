# API Endpoints Documentation

This document provides a comprehensive list of all API endpoints available in the API-Security project.

## Authentication Endpoints

All authentication endpoints are under the base path: `/api/authenticate`

### Hello World Test
- **URL**: `/api/authenticate/hello`
- **Method**: GET
- **Description**: A simple test endpoint that returns "Hello World"
- **Response**: Plain text "Hello World"

### Login
- **URL**: `/api/authenticate/login`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Login successful",
    "accessToken": "string",
    "refreshToken": "string"
  }
  ```
- **Description**: Authenticates a user and returns JWT access and refresh tokens

### Register
- **URL**: `/api/authenticate/register`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: 
  - Success: Plain text "User registered successfully."
  - Error: Plain text "Username already exists."
- **Description**: Registers a new user with the provided username and password

### Change Password
- **URL**: `/api/authenticate/changepassword`
- **Method**: PUT
- **Authentication**: Required (JWT token)
- **Request Body**:
  ```json
  {
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```
- **Response**:
  - Success: Plain text "Password changed successfully."
  - Error: Plain text "Incorrect current password."
- **Description**: Changes the password for the authenticated user

### Logout
- **URL**: `/api/authenticate/logout`
- **Method**: POST
- **Response**:
  ```json
  {
    "message": "Logged out successfully."
  }
  ```
- **Description**: Logs out the user (token should be deleted on client side)

### Validate Token
- **URL**: `/api/authenticate/validate`
- **Method**: GET
- **Authentication**: Required (JWT token)
- **Response**:
  ```json
  {
    "valid": true,
    "username": "string",
    "message": "Token is valid",
    "status": "ok"
  }
  ```
  or
  ```json
  {
    "valid": false,
    "username": null,
    "message": "Token is expired or invalid",
    "status": "expired"
  }
  ```
- **Description**: Validates the provided JWT token and returns its status

### Refresh Token
- **URL**: `/api/authenticate/refresh-token`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "refreshToken": "string"
  }
  ```
- **Response**:
  - Success:
    ```json
    {
      "accessToken": "string"
    }
    ```
  - Error: Plain text error message
- **Description**: Generates a new access token using a valid refresh token
