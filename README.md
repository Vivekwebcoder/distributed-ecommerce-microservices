# Payment Service (Money Transfer Microservice)

This project implements the **Payment Service** microservice based on the microservices architecture roadmap.

## 🚀 Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Security**: Spring Security & JWT ready
- **Database**: PostgreSQL (`payments` table)
- **Messaging**: Apache Kafka (Saga Pattern event streaming)
- **Resilience**: Resilience4j Circuit Breaker
- **Inter-service Communication**: OpenFeign & REST API
- **Containerization**: Docker & Docker Compose

---

## 🗄️ Database Schema (`payments` Table)

| Column Name | Type | Description |
|---|---|---|
| `id` | BIGINT (PK) | Auto-generated ID |
| `order_id` | VARCHAR | Order reference ID |
| `user_id` | VARCHAR | User ID initiating payment |
| `amount` | DECIMAL(12,2) | Transaction amount |
| `currency` | VARCHAR | Currency code (default: `INR`) |
| `payment_method` | VARCHAR (Enum) | `CREDIT_CARD`, `DEBIT_CARD`, `UPI`, `NET_BANKING`, `WALLET` |
| `payment_status` | VARCHAR (Enum) | `PENDING`, `SUCCESS`, `FAILED`, `REFUND_PENDING`, `REFUNDED` |
| `transaction_id` | VARCHAR | Unique reference transaction ID |
| `failure_reason` | VARCHAR | Failure error message if status is `FAILED` |
| `created_at` | TIMESTAMP | Record creation timestamp |
| `updated_at` | TIMESTAMP | Record last update timestamp |

---

## 📡 REST APIs

### 1. Pay (Process Payment)
- **URL**: `POST /api/v1/payments/pay`
- **Request Body**:
```json
{
  "orderId": "ORD-1001",
  "userId": "USER-501",
  "amount": 2500.00,
  "currency": "INR",
  "paymentMethod": "UPI"
}
```
- **Response**:
```json
{
  "success": true,
  "message": "Payment processed with status: SUCCESS",
  "data": {
    "id": 1,
    "orderId": "ORD-1001",
    "userId": "USER-501",
    "amount": 2500.00,
    "currency": "INR",
    "paymentMethod": "UPI",
    "paymentStatus": "SUCCESS",
    "transactionId": "TXN-A1B2C3D4E5F6",
    "createdAt": "2026-07-31T12:00:00"
  }
}
```

### 2. Refund (Process Refund)
- **URL**: `POST /api/v1/payments/refund`
- **Request Body**:
```json
{
  "orderId": "ORD-1001",
  "amount": 2500.00,
  "reason": "Customer requested cancellation"
}
```

### 3. Payment Status
- **URL**: `GET /api/v1/payments/{paymentId}/status`
- **URL (by Order ID)**: `GET /api/v1/payments/order/{orderId}/status`

### 4. Payment History
- **URL**: `GET /api/v1/payments/history?userId=USER-501&page=0&size=10`

---

## 🔄 Kafka Events & Saga Integration

### Published Events:
1. `PaymentSuccess`: Published when payment transaction is successfully authorized.
2. `PaymentFailed`: Published when transaction fails or is rejected.
3. `RefundCompleted`: Published when refund compensation flow succeeds.

### Consumed Events:
1. `PaymentInitiated`: Listened from Order Service to trigger auto-payment in Saga flow.
2. `RefundInitiated`: Listened from Order/Inventory Service to trigger auto-refund compensation.

---

## 🛠️ How to Run Locally

### Option A: Using Docker Compose (Recommended)
```bash
docker-compose up -d --build
```

### Option B: Maven Local Run
1. Ensure PostgreSQL is running on `localhost:5432` with database `payment_db`.
2. Ensure Kafka is running on `localhost:9092`.
3. Run the application:
```bash
mvn spring-boot:run
```
