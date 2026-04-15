# 📚 Online Bookstore: Full-Stack Web Application

A complete, production-ready online bookstore application featuring a robust Java Spring Boot backend, a React frontend, and a PostgreSQL database. The entire stack is containerized for seamless, one-command deployment.

---

## 🌟 Key Features

* **Complete E-Commerce Flow:** Comprehensive management of books, user carts, order processing, authentication, and publisher inventory.
* **Production-Grade Architecture:** Strictly enforces a layered architecture utilizing the DTO (Data Transfer Object) pattern, request/response mappers, and bean validation to ensure secure and clean data flow.
* **Resilient Error Management:** Implements centralized, global exception handling via `@ControllerAdvice` to provide standardized, predictable REST API error responses.
* **Containerized Deployment:** The entire ecosystem (Backend, Frontend, and Database) is orchestrated using Docker Compose, eliminating "it works on my machine" issues.

---

## 🏗️ System Architecture

The backend is built with Spring Boot 4 and follows a strict layered design:

* **Presentation Layer (8 REST Controllers):** Handles HTTP requests, enforces Bean Validation, and maps incoming JSON to internal models.
* **Business Layer (6 Service Interfaces):** Encapsulates core e-commerce logic, transaction management, and validation rules.
* **Data Access Layer (11 JPA Repositories):** Interfaces with PostgreSQL via Spring Data JPA/Hibernate for efficient, ORM-based data persistence.

---

## 🚀 Quick Start (Docker)

You can spin up the entire full-stack application with a single command. 

### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed.

### Installation

1. Clone the repository:
   ```bash
   git clone [https://github.com/Ahmed-Elghazaly/bookstore.git](https://github.com/Ahmed-Elghazaly/bookstore.git)
   cd bookstore