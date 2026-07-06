# E-commerce Order Management API

API REST de e-commerce com gestão de pedidos, processamento assíncrono de pagamentos e cache distribuído. Desenvolvida como projeto de portfólio para demonstrar arquitetura backend aplicada a um cenário realista de negócio.

## 🚀 Funcionalidades

- **Autenticação e autorização** via JWT com roles (ADMIN / CUSTOMER)
- **Gestão de catálogo** de produtos com cache Redis
- **Gestão de pedidos** com validação de estoque e cálculo automático de valores
- **Processamento assíncrono de pagamentos** via RabbitMQ (fila de eventos)
- **Testes automatizados** (unitários com Mockito + integração)
- **Documentação interativa** via Swagger/OpenAPI
- **Containerização completa** com Docker Compose (app + Postgres + RabbitMQ + Redis)

## 🛠️ Stack técnica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| Persistência | Spring Data JPA + PostgreSQL |
| Segurança | Spring Security + JWT |
| Mensageria | RabbitMQ |
| Cache | Redis |
| Testes | JUnit 5, Mockito, Testcontainers |
| Documentação | Swagger / OpenAPI |
| Containerização | Docker, Docker Compose |

## 🏗️ Arquitetura

O fluxo de compra segue este caminho:

1. Cliente se autentica e recebe um token JWT
2. Cliente cria um pedido → sistema valida estoque e calcula o total no backend (nunca confia no valor enviado pelo cliente)
3. Cliente paga o pedido → pagamento é processado e um evento é publicado no RabbitMQ
4. Um listener assíncrono consome o evento e atualiza o status do pedido, sem bloquear a resposta HTTP ao cliente
5. Consultas ao catálogo de produtos são cacheadas no Redis para reduzir carga no banco

## 📋 Pré-requisitos

- Docker e Docker Compose instalados

## ▶️ Como rodar

Clone o repositório e execute:

```bash
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

Documentação interativa (Swagger): `http://localhost:8080/swagger-ui/index.html`

Painel do RabbitMQ: `http://localhost:15672` (usuário: `admin`, senha: `admin123`)

## 🔑 Autenticação

1. Registre um usuário: `POST /api/auth/register`
2. Faça login: `POST /api/auth/login` (retorna um token JWT)
3. Use o token no header: `Authorization: Bearer <token>` nas demais requisições

## 🧪 Rodando os testes

```bash
mvn test
```

## 📌 Endpoints principais

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/api/auth/register` | Registrar usuário | Público |
| POST | `/api/auth/login` | Login | Público |
| GET | `/api/products` | Listar produtos (cacheado) | Autenticado |
| POST | `/api/orders` | Criar pedido | Autenticado |
| GET | `/api/orders/my` | Meus pedidos | Autenticado |
| POST | `/api/payments` | Pagar pedido | Autenticado |
| GET | `/api/users` | Listar usuários | Admin |

## 👤 Autor

Kevin Rodrigues
