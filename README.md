# E-commerce Order Management API

A REST API for e-commerce with order management, asynchronous payment processing, and distributed caching. Built as a portfolio project to demonstrate backend architecture applied to a realistic business scenario.

## 🚀 Features

- **Authentication and authorization** via JWT with role-based access (ADMIN / CUSTOMER)
- **Product catalog management** with Redis caching
- **Order management** with stock validation and automatic price calculation
- **Asynchronous payment processing** via RabbitMQ (event queue)
- **Automated tests** (unit tests with Mockito + integration tests)
- **Interactive documentation** via Swagger/OpenAPI
- **Full containerization** with Docker Compose (app + Postgres + RabbitMQ + Redis)

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Persistence | Spring Data JPA + PostgreSQL |
| Security | Spring Security + JWT |
| Messaging | RabbitMQ |
| Cache | Redis |
| Testing | JUnit 5, Mockito, Testcontainers |
| Documentation | Swagger / OpenAPI |
| Containerization | Docker, Docker Compose |

## 🏗️ Architecture

The purchase flow works as follows:

1. Customer authenticates and receives a JWT token
2. Customer creates an order → the backend validates stock and calculates the total (never trusts values sent by the client)
3. Customer pays for the order → the payment is processed and an event is published to RabbitMQ
4. An asynchronous listener consumes the event and updates the order status, without blocking the HTTP response to the client
5. Product catalog queries are cached in Redis to reduce database load

## 📋 Prerequisites

- Docker and Docker Compose installed

## ▶️ How to run

Clone the repository and run:

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

Interactive documentation (Swagger): `http://localhost:8080/swagger-ui/index.html`

RabbitMQ management panel: `http://localhost:15672` (user: `admin`, password: `admin123`)

## 🔑 Authentication

1. Register a user: `POST /api/auth/register`
2. Log in: `POST /api/auth/login` (returns a JWT token)
3. Use the token in the header: `Authorization: Bearer <token>` for subsequent requests

## 🧪 Running the tests

```bash
mvn test
```

## 📌 Main endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Register user | Public |
| POST | `/api/auth/login` | Log in | Public |
| GET | `/api/products` | List products (cached) | Authenticated |
| POST | `/api/orders` | Create order | Authenticated |
| GET | `/api/orders/my` | My orders | Authenticated |
| POST | `/api/payments` | Pay for order | Authenticated |
| GET | `/api/users` | List users | Admin |

## 👤 Author

Kevin Rodrigues
