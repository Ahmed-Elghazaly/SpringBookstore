
# 📚 GoldenBooks: Full-Stack E-Commerce Platform

A production-ready, full-stack online bookstore application. Built with a robust Java Spring Boot REST API, a responsive React.js frontend, and an advanced PostgreSQL database. The entire ecosystem is containerized for seamless, one-command deployment.

---

## 🌟 Key Features

### 🛡️ Backend Architecture (Spring Boot)
* **Strict Layered Design:** Clean separation of concerns across Presentation (Controllers), Business (Services), and Data Access (Repositories) layers.
* **Data Security & Integrity:** Utilizes the DTO (Data Transfer Object) pattern and Spring Boot Bean Validation to strictly control data entering and leaving the API.
* **Global Error Handling:** Implements a centralized `@ControllerAdvice` architecture for standardized, predictable REST API error responses.
* **Role-Based Access Control:** Distinct authentication flows and capabilities for `CUSTOMER` and `ADMIN` users.

### 🗄️ Advanced Database Design (PostgreSQL)
* **Automated Inventory Management:** Custom PL/pgSQL triggers (`prevent_negative_stock`, `auto_place_publisher_order`) automatically monitor thresholds and place pending restock orders when inventory drops.
* **Real-Time Analytics:** Complex native SQL queries generate real-time reporting for top-spending customers, top-selling books, and temporal sales data.
* **Data Integrity:** Strict foreign key constraints and cascading deletes ensure relational integrity across 9 interconnected tables.

### 💻 Frontend Experience (React + Tailwind)
* **Customer Portal:** Browse catalogs, dynamic multi-parameter search, shopping cart management, and order history tracking.
* **Admin Dashboard:** Real-time analytics, inventory management, category/publisher configurations, and one-click publisher order fulfillment.

---

## 🚀 Quick Start (Docker)

The entire application is orchestrated using Docker Compose. No local Java or Node installations are required.

### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)

### Installation & Deployment

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Ahmed-Elghazaly/SpringBookstore.git
   cd springbookstore
   ```

2. **Boot the cluster:**
   ```bash
   docker-compose up -d --build
   ```

3. **Access the application:**
   * **Frontend UI:** `http://localhost:3000` *(Served via Nginx)*
   * **Backend API:** `http://localhost:8080/api`
   * **Database:** `localhost:5432`

*Note: The database is automatically seeded with mock data, publishers, and books on the first boot.*

---

## 🛠️ Tech Stack

**Frontend**
* React.js 18 (Vite)
* Tailwind CSS
* Lucide React (Icons)
* Axios

**Backend**
* Java 21
* Spring Boot (WebMVC, Data JPA, Validation)
* Hibernate ORM

**Database & Infrastructure**
* PostgreSQL 18
* Docker & Docker Compose
* Nginx (Frontend Reverse Proxy)

---

## 👨‍💻 Default Test Accounts

To explore the application without registering, use the pre-seeded test accounts:

* **Admin Account:**
  * Username: `admin`
  * Password: `admin123`
* **Customer Account:**
  * Username: `a`
  * Password: `a`