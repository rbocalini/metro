# Backend — Status Metrô

## Purpose

Spring Boot REST API that proxies the Grupo CCR public line status API
and serves pre-processed data to the Svelte frontend.

## Tech Stack

- Java 21
- Spring Boot 4.0.3
- Spring Cache + Caffeine (30s TTL in-memory cache)
- springdoc-openapi (Swagger UI at /swagger-ui.html)
- Maven Wrapper for builds

## Build & Run

```bash
# From this directory:
./mvnw spring-boot:run

# Run tests (TDD — tests must be written first):
./mvnw test

# Package:
./mvnw package
```

Server starts on `http://localhost:8080`.

## Package Structure

```
src/main/java/com/statusmetro/
├── StatusMetroApplication.java   # Entry point, @EnableCaching
├── config/
│   ├── CacheConfig.java          # Caffeine cache (30s TTL)
│   ├── RestClientConfig.java     # RestClient bean for external API
│   ├── WebConfig.java            # CORS configuration
│   └── GlobalExceptionHandler.java # Maps exceptions to HTTP responses
├── controller/
│   └── LineStatusController.java # GET /api/line-status
├── dto/
│   ├── LineStatusResponse.java   # Top-level response DTO
│   ├── LineGroupDTO.java         # Group (Metrô / Trens)
│   ├── LineDTO.java              # Single line
│   └── LineStatusDTO.java        # Status code + label + description
└── service/
    └── LineStatusService.java    # Fetches CCR API, maps, groups, caches
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/line-status | Returns all line statuses grouped into Metrô and Trens |

## External API

- URL: `https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP`
- No authentication required
- Response is cached for 30 seconds to avoid excessive calls

## Conventions

- TDD mandatory: write jUnit test → test fails → implement → test passes
- DTOs are Java records
- Swagger annotations on all controller methods
- No database — this is a read-through proxy
- Line grouping: Metrô = lines 1-5, 15; Trens = lines 7-13

## Configuration

Key properties in `application.properties`:
- `statusmetro.api.url` — external API URL
- `server.port` — defaults to 8080
