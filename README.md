This project is a microservices-based event-driven system designed to handle order processing using asynchronous communication.

Instead of tightly coupling services with direct API calls, the system uses a message broker to ensure scalability, fault tolerance, and loose coupling between services.

## Services

- `order-service` runs on port `8080`
- `payment-service` runs on port `8081`
- `notification-service` runs on port `8082`
- `inventory-service` runs on port `8083`

## Infrastructure

Start RabbitMQ and MongoDB:

```bash
docker compose up -d
```

RabbitMQ management UI:

```text
http://localhost:15672
username: guest
password: guest
```

MongoDB:

```text
mongodb://localhost:27017/orderdb
```

## Main Event Flow

```text
Client -> order-service -> MongoDB
order-service -> OrderCreatedEvent -> RabbitMQ
RabbitMQ -> inventory-service
inventory-service -> InventoryReservedEvent or InventoryFailedEvent -> RabbitMQ
RabbitMQ -> order-service updates order status
RabbitMQ -> payment-service
RabbitMQ -> notification-service
payment-service -> PaymentCompletedEvent -> RabbitMQ
RabbitMQ -> order-service marks order as PAID
```

## Inventory Event Flow

```text
OrderCreatedEvent
  -> inventory-service checks stock
  -> if stock is available:
       reduce stock
       publish InventoryReservedEvent
       order-service marks order as INVENTORY_RESERVED
  -> if stock is not available:
       publish InventoryFailedEvent
       order-service marks order as INVENTORY_FAILED
```

## Create Order Example

```bash
curl -X POST http://localhost:8080/order/create \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"U101\",\"productId\":\"P101\",\"quantity\":2}"
```

