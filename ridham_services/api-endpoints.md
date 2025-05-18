# Water Services API Endpoints

This document provides a comprehensive list of all API endpoints available in the Water Services application.

## Authentication Service
Base URL: `/api/authenticate`

| Method | Endpoint | Description | Authentication | Request | Response |
|--------|----------|-------------|----------------|---------|----------|
| POST | `/api/authenticate/login` | User login | None | Credentials | JWT Token |
| POST | `/api/authenticate/register` | User registration | None | User details | Registration confirmation |
| POST | `/api/authenticate/refresh-token` | Refresh authentication token | None | Refresh token | New JWT Token |
| POST | `/api/authenticate/validate` | Validate token | None | JWT Token | Validation result |

## Monitoring Service
Base URL: `/api/monitoring`

| Method | Endpoint | Description | Authentication | Request | Response |
|--------|----------|-------------|----------------|---------|----------|
| GET | `/api/monitoring/records` | Retrieve all water quality records | JWT Token required | None | List of WaterQuality records |
| GET | `/api/monitoring/records/latest` | Retrieve the latest water quality record | JWT Token required | None | WaterQuality record |

## Quality Analysis Service
Base URL: `/api/quality-analysis`

| Method | Endpoint | Description | Authentication | Request | Response |
|--------|----------|-------------|----------------|---------|----------|
| GET | `/api/quality-analysis/records/latest/analysis` | Retrieve the latest analysis result | JWT Token required | None | AnalysisResult |
| GET | `/api/quality-analysis/thresholds` | Retrieve water quality thresholds | JWT Token required | None | Map of threshold values |

## API Gateway Fallbacks
Base URL: `/fallback`

| Method | Endpoint | Description | Authentication | Request | Response |
|--------|----------|-------------|----------------|---------|----------|
| GET | `/fallback/quality-analysis` | Fallback for Quality Analysis service | None | None | Service unavailable message |
| POST | `/fallback/quality-analysis` | Fallback for Quality Analysis service (POST) | None | None | Service unavailable message |
| GET | `/fallback/monitoring` | Fallback for Monitoring service | None | None | Service unavailable message |
| POST | `/fallback/monitoring` | Fallback for Monitoring service (POST) | None | None | Service unavailable message |