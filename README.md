# MiniS3

A lightweight file storage service built with Spring Boot. Upload, download, and manage files with metadata storage.

## Features

- File upload/download/delete with validation (size & type limits)
- Metadata storage in database
- REST API with pagination support
- Tags support for file organization

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21 + Spring Boot 4.0.2 |
| Database | H2 (dev) / PostgreSQL (prod) |
| File Storage | Local filesystem |
| Testing | JUnit + Mockito |

## Getting Started

### Prerequisites

- Java 21
- Maven

### Run the Application

```bash
# Build the project
./mvnw clean package

# Run the application (starts on port 8080)
./mvnw spring-boot:run

# Run tests
./mvnw test
```

## API Endpoints

| Operation | Endpoint | Description |
|-----------|----------|-------------|
| Upload file | `POST /api/v1/files` | Multipart file upload (returns 201) |
| List files | `GET /api/v1/files` | Returns paginated metadata list |
| Download file | `GET /api/v1/files/{id}` | Returns file bytes |
| Update metadata | `PUT /api/v1/files/{id}` | Update tags/filename |
| Delete file | `DELETE /api/v1/files/{id}` | Remove file + DB entry |

## API Examples

```bash
# Upload
curl -F "file=@image.png" http://localhost:8080/api/v1/files

# List (with pagination)
curl "http://localhost:8080/api/v1/files?page=0&size=10"

# Download
curl -OJ http://localhost:8080/api/v1/files/{id}

# Update
curl -X PUT -H "Content-Type: application/json" \
  -d '{"originalFilename":"new-name.png","tags":"tag1,tag2"}' \
  http://localhost:8080/api/v1/files/{id}

# Delete
curl -X DELETE http://localhost:8080/api/v1/files/{id}
```

**Sample Response (list/upload/update):**
```json
{"id":"uuid","originalFilename":"image.png","size":1024,"uploadDate":"2025-01-15T10:30:00Z","tags":"tag1","contentType":"image/png"}
```

## Architecture

```
Browser (static/index.html)
       |
FileController (/api/v1/files)
       |
FileStorageService -> FileStorageServiceImpl
       |
    +--+--+
    |     |
Filesystem   H2 Database
(uploads/)   (FileMetadata)
```

## Key URLs (when running)

- Web UI: http://localhost:8080/
- API: http://localhost:8080/api/v1/files
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:minis3db`
  - User: `sa`
  - Password: (empty)

## Project Structure

```
src/main/java/com/haris/minis3/
├── controller/     # REST controllers
├── dto/            # Data transfer objects
├── entity/         # JPA entities
├── exception/      # Exception handlers
├── mapper/         # Entity-DTO mappers
├── repository/     # Spring Data repositories
└── service/        # Business logic
```

## License

MIT
