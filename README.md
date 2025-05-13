# plip-monitor
Service/App Monitoring SaaS

## 🔧 Setup & Build Instructions

### 📦 Requirements
- Java 17+
- Docker & Docker Compose
- Maven (optional if building inside Docker)

### 🚀 Local Setup (via Docker Compose)
```bash
git clone https://github.com/venkatrajdamo/plip-monitor.git
cd plip-monitor
docker-compose up --build
```

- Access API: [http://localhost:8080](http://localhost:8080)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 🛠️ Build with Maven (Manual)
```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

---

## 🧱 Architecture Overview

```
+----------------------+
|     API Layer        | <-- Spring Boot REST Controllers
+----------------------+
           |
           v
+----------------------+       +----------------------+
|   Service Layer      | <---> |  Scheduler (60s loop)|
+----------------------+       +----------------------+
           |
           v
+------------------------------+
|   JPA Repositories           |
|   (Monitor, CheckResult)     |
+------------------------------+
           |
           v
+------------------------------+
|     PostgreSQL (Docker)      |
+------------------------------+
```

### 🧩 Components
- `Monitor`: Defines a target endpoint to be monitored (URL, method, interval)
- `CheckResult`: Stores timestamped uptime results (status code, response time)
- `MonitorScheduler`: Background task that pings active monitors and logs results
- `CheckResultService`: Handles storage & aggregation of monitor results
- `Swagger UI`: Auto-docs for all endpoints
- `Docker`: App + DB orchestration
- `Jenkins` (coming soon): CI/CD automation

---

## 📈 Upcoming Features
- GraphQL and gRPC monitor support
- JWT-based authentication
- Retry logic & alerting (email/webhooks)
- K8s deployment config
