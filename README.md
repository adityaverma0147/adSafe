# adSafe

adSafe is a production-grade Real-Time Bidding (RTB) Ad Auction Platform designed to process programmatic advertising bids with ultra-low latency. It features a distributed backend architecture and a modern, high-performance dashboard for campaign management and analytics.

The platform was engineered to handle high-concurrency environments, utilizing asynchronous scatter-gather patterns, memory caching, and event-driven data flows to meet strict sub-100ms response requirements.

## Architecture & Tech Stack

### Backend
* Java 21 & Spring Boot 3
* PostgreSQL (Relational Data & Budgets)
* Redis (In-Memory Caching)
* Apache Kafka (Event-Driven Analytics & Decoupling)
* Spring Security (JWT-based Authentication)
* Hibernate / Spring Data JPA (Pessimistic Locking for Financial Integrity)
* Spring Boot Actuator & Micrometer (Prometheus Metrics)
* Swagger / OpenAPI (API Documentation)

### Frontend
* Next.js 15+ (App Router)
* TypeScript
* React Context API (State Management)
* Tailwind CSS v4 (Styling & Glassmorphism)
* Recharts (Data Visualization)
* Axios (API Integration)

### Infrastructure
* Docker & Docker Compose (Multi-container deployment)

## Key Features

1. Real-Time Bidding Engine
The core of adSafe is its AuctionService, which uses Java's CompletableFuture API to perform parallel scatter-gather operations. It simultaneously queries multiple Demand-Side Platforms (DSPs) for bids.

2. Strict Latency Enforcement
To adhere to RTB industry standards, the bidding engine enforces a hard 50ms timeout on all downstream DSP requests. Any bid that takes longer than 50ms is automatically dropped to ensure the platform never hangs.

3. Distributed State & Caching
Campaign data and available budgets are aggressively cached in Redis. This prevents slow disk I/O operations from blocking the critical path of the auction.

4. Event-Driven Analytics
Auction outcomes and telemetry data are not written synchronously to the database. Instead, they are published to an Apache Kafka topic. A background consumer processes these events to update publisher revenues and DSP spend, completely decoupling analytical writes from the real-time bidding path.

5. Financial Integrity
adSafe uses database-level pessimistic locking via JPA to ensure that concurrent auction wins do not result in race conditions when deducting from campaign budgets.

6. Modern Analytics Dashboard
The Next.js frontend provides a secure, JWT-authenticated dashboard featuring real-time data visualization. It displays live metrics including P99 latency, win rates, active campaigns, and total ad spend using Recharts.

7. Production Ready
The system is fully containerized using Docker. Multi-stage Dockerfiles ensure minimal image sizes. It also includes built-in observability endpoints via Prometheus and Actuator, making it ready for Kubernetes deployment.

## Getting Started

### Prerequisites
* Java 21+
* Node.js 18+
* Maven
* Docker (for infrastructure services)

### Local Development Setup

1. Start Infrastructure Services
You can spin up PostgreSQL, Redis, and Kafka using the provided Docker Compose file:
```bash
docker-compose up -d postgres redis kafka
```

2. Start the Backend
```bash
cd backend
mvn spring-boot:run
```
The backend will automatically create the database schema and seed it with initial data.

3. Start the Frontend
```bash
cd frontend
npm install
npm run dev
```

The application will be accessible at http://localhost:3000.

## API Documentation

Once the backend is running, the Swagger UI is automatically generated and can be accessed to view all REST endpoints:
http://localhost:8080/swagger-ui/index.html

## Testing

The platform includes a suite of unit tests, specifically targeting the concurrency and latency controls of the Auction Engine using Mockito.
```bash
cd backend
mvn test
```
