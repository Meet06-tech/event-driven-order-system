This project is a microservices-based event-driven system designed to handle order processing using asynchronous communication.

Instead of tightly coupling services with direct API calls, the system uses a message broker to ensure scalability, fault tolerance, and loose coupling between services.

Problem Statement

Traditional monolithic systems struggle with:

Tight coupling between components
Poor scalability
Difficult failure handling

This project solves these issues using an event-driven architecture where services communicate via events instead of direct calls.

 Architecture
Core Concept

Each service:

Works independently
Communicates via events
Does NOT depend on other services directly
Services Included
Order Service → Creates orders and publishes events
Inventory Service → Listens for order events and updates stock
Notification Service → Sends notifications after order processing
 Event Flow
User places an order
Order Service publishes an event
Inventory Service consumes the event
Inventory updates stock
Notification Service sends confirmation
 Tech Stack
Backend: Java (Spring Boot)
Messaging Broker: RabbitMQ
Architecture Style: Event-Driven Microservices
Build Tool: Maven / Gradle
 Messaging Design
Exchange Type Used
Direct Exchange → Routes messages using routing keys
Why Not Fanout?

Fanout sends messages to all queues blindly.
This project requires controlled routing, so Direct Exchange is used.

 Key Concepts Implemented
Asynchronous communication
Loose coupling between services
Event publishing and consumption
Message queues and routing keys
Microservices separation

Project Structure
event-driven-order-system/
│
├── order-service/
├── inventory-service/
├── notification-service/
│
└── common-config/

How to Run
Start RabbitMQ
Run all services:
Order Service
Inventory Service
Notification Service
Send a request to create an order
Observe event flow between services
 Features
Decoupled services
Scalable architecture
Fault isolation
Real-time event processing
 Limitations (Be honest — interviewers respect this)
No API Gateway
No Service Discovery
No Retry / Dead Letter Queue
Basic logging only
 Future Improvements

This is where you separate yourself from average students:

Add API Gateway (Spring Cloud Gateway)
Implement Service Discovery (Eureka)
Add Retry Mechanism & Dead Letter Queue
Dockerize services
Add centralized logging and monitoring
 Why This Project Matters

This project demonstrates:

Understanding of real-world backend architecture
Ability to design scalable systems
Knowledge of asynchronous communication