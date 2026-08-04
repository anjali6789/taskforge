# TaskForge - Enterprise Task Management System

A hands-on Java learning project building a production-ready Task Management System from scratch.

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose

### Start Infrastructure
```bash
docker compose up -d
```

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
cd taskforge-api
mvn spring-boot:run
```

### Access Points
| Service | URL |
|---------|-----|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin/admin) |
| Kafka UI | http://localhost:8090 |
| RabbitMQ UI | http://localhost:15672 (guest/guest) |
| MailHog | http://localhost:8025 |
| Jaeger UI | http://localhost:16686 |

## Project Structure
```
taskforge/
├── pom.xml                    # Parent POM
├── docker-compose.yml         # Local infrastructure
├── taskforge-common/          # Shared DTOs, utilities
├── taskforge-core/            # Business logic, entities
├── taskforge-api/             # REST controllers, main app
└── taskforge-notification/    # Kafka, RabbitMQ, email
```

## Learning Phases
See [LEARNING_PLAN.md](../LEARNING_PLAN.md) for the full curriculum.
See [PROGRESS_TRACKER.md](../PROGRESS_TRACKER.md) to track your progress.
