# SiddusBank Backend API

A comprehensive REST API built with Spring Boot for banking operations including user authentication, account management, and financial transactions.

---

## 🌐 Live Demo

| Service | URL |
|---------|-----|
| **Base API** | https://siddusbank-backend-production.up.railway.app |
| **Test Endpoint** | https://siddusbank-backend-production.up.railway.app/api/auth/test |
| **GitHub Repository** | https://github.com/satishveesam/siddusbank-backend |

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Local Development Setup](#local-development-setup)
- [Deployment](#deployment)
- [Key Achievements](#key-achievements)
- [About the Developer](#about-the-developer)

---

## 📖 Overview

SiddusBank Backend is a secure, production-ready REST API that powers the SiddusBank banking application. It handles user authentication, account management, deposits, withdrawals, transfers, and transaction history. The API uses **JWT** for stateless authentication and **MySQL** for persistent data storage.

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based authentication with 24-hour token expiry
- User registration with email and username validation
- Secure password encryption using BCrypt
- Token-based authorization for all protected endpoints
- Password change with current password verification
- Account deactivation (soft delete)

### 👤 User Profile Management
- View and update profile details (name, email, phone, address, DOB)
- User statistics and profile completion status
- Member since date tracking

### 💰 Account Management
- Create new bank accounts (Savings, Checking)
- View all accounts belonging to the logged-in user
- View individual account details
- Delete/deactivate accounts (only allowed if balance is zero)
- Currency support in Indian Rupees (INR)

### 💸 Transaction Management
- Deposit money into any account
- Withdraw money with balance validation
- Transfer money between accounts
- View complete transaction history with timestamps
- Automatic transaction recording for all operations

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Security | 3.2.0 | Authentication & authorization |
| Spring Data JPA | 3.2.0 | Database ORM |
| JWT (JJWT) | 0.11.5 | Token-based authentication |
| MySQL | 8.0+ | Relational database |
| Hibernate | 6.3.1 | ORM implementation |
| Maven | 3.8+ | Build automation |
| Railway | - | Cloud deployment platform |

---

## 📁 Project Structure

BankingApplications/
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── BankingApplications/
│ │ ├── BankingApplicationsApplication.java
│ │ ├── controller/
│ │ │ ├── AccountController.java
│ │ │ └── AuthController.java
│ │ ├── dto/
│ │ │ ├── AccountDto.java
│ │ │ ├── AuthResponse.java
│ │ │ ├── ChangePasswordRequest.java
│ │ │ ├── LoginRequest.java
│ │ │ ├── RegisterRequest.java
│ │ │ ├── TransferRequest.java
│ │ │ └── UpdateProfileRequest.java
│ │ ├── entity/
│ │ │ ├── Account.java
│ │ │ ├── Transaction.java
│ │ │ └── User.java
│ │ ├── repository/
│ │ │ ├── AccountRepository.java
│ │ │ ├── TransactionRepository.java
│ │ │ └── UserRepository.java
│ │ ├── security/
│ │ │ ├── JwtFilter.java
│ │ │ ├── JwtUtil.java
│ │ │ └── SecurityConfig.java
│ │ └── service/
│ │ ├── AccountService.java
│ │ └── CustomUserDetailsService.java
│ └── resources/
│ ├── application.properties
│ └── application-prod.properties
├── pom.xml
├── Procfile
├── railway.json
└── README.md

---

## 📚 API Documentation

### Base URL

https://siddusbank-backend-production.up.railway.app/api


### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register a new user | ❌ No |
| POST | `/auth/login` | Login and receive JWT token | ❌ No |
| GET | `/auth/test` | Test backend connection | ❌ No |
| GET | `/auth/profile` | Get current user profile | ✅ Yes |
| PUT | `/auth/profile` | Update user profile | ✅ Yes |
| POST | `/auth/change-password` | Change user password | ✅ Yes |
| DELETE | `/auth/account` | Deactivate user account | ✅ Yes |
| GET | `/auth/statistics` | Get user statistics | ✅ Yes |

### Account Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/accounts` | Get all accounts for logged-in user | ✅ Yes |
| GET | `/accounts/{id}` | Get account by ID | ✅ Yes |
| POST | `/accounts` | Create a new bank account | ✅ Yes |
| POST | `/accounts/{id}/deposit` | Deposit money into account | ✅ Yes |
| POST | `/accounts/{id}/withdraw` | Withdraw money from account | ✅ Yes |
| POST | `/accounts/transfer` | Transfer money between accounts | ✅ Yes |
| DELETE | `/accounts/{id}` | Delete/deactivate account | ✅ Yes |
| GET | `/accounts/{id}/transactions` | Get transaction history | ✅ Yes |

---

## 🗄️ Database Schema

### Users Table

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    address VARCHAR(500),
    date_of_birth DATE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

## Accounts Table

CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_type VARCHAR(50) NOT NULL,
    balance DECIMAL(19,2) DEFAULT 0.00,
    currency VARCHAR(3) DEFAULT 'INR',
    active BOOLEAN DEFAULT TRUE,
    user_id BIGINT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

## Transactions Table

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) DEFAULT 'COMPLETED',
    account_id BIGINT,
    to_account_id BIGINT,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

### 🔧 Local Development Setup

## Prerequisites

Java JDK 17 or higher

Maven 3.8 or higher

MySQL 8.0 or higher

Installation Steps

## 1.Clone the repository

git clone https://github.com/satishveesam/siddusbank-backend.git
cd siddusbank-backend

## 2.Create the database

CREATE DATABASE siddu;

## 2.Configure database credentials
## Update src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/siddu?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

## Build and run the application

mvn clean install
mvn spring-boot:run

