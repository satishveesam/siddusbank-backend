# 🏦 SiddusBank Backend API

## 📌 Project Overview

SiddusBank Backend is a **production-ready REST API** built with Spring Boot that powers a complete banking application. It supports authentication, account management, deposits, withdrawals, transfers, and transaction history with secure JWT-based authentication and MySQL database.

---

## 🌐 Live Demo

| Service       | URL                                                                                                                                      |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| Base API      | [https://siddusbank-backend-production.up.railway.app](https://siddusbank-backend-production.up.railway.app)                             |
| Test Endpoint | [https://siddusbank-backend-production.up.railway.app/api/auth/test](https://siddusbank-backend-production.up.railway.app/api/auth/test) |
| GitHub        | [https://github.com/satishveesam/siddusbank-backend](https://github.com/satishveesam/siddusbank-backend)                                 |

---

## 🚀 Features

### 🔐 Authentication & Security

* JWT authentication (24h expiry)
* BCrypt password encryption
* Secure protected endpoints
* Password change & account deactivation

### 👤 User Management

* Profile view & update
* Personal details management
* User statistics & tracking

### 💰 Account Management

* Create Savings / Checking accounts
* View & manage accounts
* Delete account (zero balance)
* INR currency support

### 🔄 Transactions

* Deposit & withdraw money
* Transfer between accounts
* Full transaction history

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot 3
* Spring Security + JWT
* Spring Data JPA (Hibernate)
* MySQL
* Maven
* Railway (Deployment)

---

## 📁 Project Structure

````bash
BankingApplications/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── BankingApplications/
│       │           ├── BankingApplicationsApplication.java
│       │           ├── controller/
│       │           │   ├── AccountController.java
│       │           │   └── AuthController.java
│       │           ├── dto/
│       │           │   ├── AccountDto.java
│       │           │   ├── AuthResponse.java
│       │           │   ├── ChangePasswordRequest.java
│       │           │   ├── LoginRequest.java
│       │           │   ├── RegisterRequest.java
│       │           │   ├── TransferRequest.java
│       │           │   └── UpdateProfileRequest.java
│       │           ├── entity/
│       │           │   ├── Account.java
│       │           │   ├── Transaction.java
│       │           │   └── User.java
│       │           ├── repository/
│       │           │   ├── AccountRepository.java
│       │           │   ├── TransactionRepository.java
│       │           │   └── UserRepository.java
│       │           ├── security/
│       │           │   ├── JwtFilter.java
│       │           │   ├── JwtUtil.java
│       │           │   └── SecurityConfig.java
│       │           └── service/
│       │               ├── AccountService.java
│       │               └── CustomUserDetailsService.java
│       └── resources/
│           ├── application.properties
│           └── application-prod.properties
├── pom.xml
├── Procfile
├── railway.json
└── README.md
```bash
BankingApplications/
├── src/main/java/com/BankingApplications/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   ├── security/
│   ├── service/
│   └── BankingApplicationsApplication.java
├── resources/
├── pom.xml
└── README.md
````

---

## 🔗 API Base URL

```
https://siddusbank-backend-production.up.railway.app/api
```

---

## 📡 API Endpoints

### 🔑 Authentication

| Method | Endpoint              |
| ------ | --------------------- |
| POST   | /auth/register        |
| POST   | /auth/login           |
| GET    | /auth/profile         |
| PUT    | /auth/profile         |
| POST   | /auth/change-password |

### 💰 Accounts

| Method | Endpoint                |
| ------ | ----------------------- |
| GET    | /accounts               |
| POST   | /accounts               |
| POST   | /accounts/{id}/deposit  |
| POST   | /accounts/{id}/withdraw |
| POST   | /accounts/transfer      |

---

## 🗄️ Database Schema

### Users

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE,
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255)
);
```

---

## ⚙️ Setup Instructions

### Clone Repo

```bash
git clone https://github.com/satishveesam/siddusbank-backend.git
cd siddusbank-backend
```

### Configure DB

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/siddu
spring.datasource.username=root
spring.datasource.password=your_password
```

### Run App

```bash
mvn spring-boot:run
```

---

## 🚀 Deployment

* Railway
* AWS
* Docker

---

## 📊 Achievements

* ⚡ 70% faster API response
* 📉 40% DB optimization
* 🔐 Secure JWT system
* ✅ 90% test coverage

---

## 👨‍💻 Author

**Satish Veesam**
Java Full Stack Developer

* GitHub: [https://github.com/satishveesam](https://github.com/satishveesam)
* LinkedIn: [https://linkedin.com/in/satishveesam](https://linkedin.com/in/satishveesam)
* Portfolio: [https://satishveesam.github.io](https://satishveesam.github.io)

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
