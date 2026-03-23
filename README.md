# SiddusBank Backend API

## 📋 Overview

SiddusBank Backend is a comprehensive REST API built with **Spring Boot** that powers the SiddusBank banking application. It provides secure endpoints for user authentication, account management, and financial transactions. The API uses **JWT (JSON Web Tokens)** for authentication and **MySQL** for data persistence.

---

## 🌐 Live API

| Service | URL |
|---------|-----|
| **Base API** | https://siddusbank-backend-production.up.railway.app |
| **Test Endpoint** | https://siddusbank-backend-production.up.railway.app/api/auth/test |
| **GitHub Repository** | https://github.com/satishveesam/siddusbank-backend |

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based authentication with 24-hour token expiry
- User registration with validation
- Secure login with password encryption (BCrypt)
- Token-based authorization for protected endpoints
- Password change functionality
- Account deactivation (soft delete)

### 👤 User Profile Management
- View profile details
- Update personal information (name, email, phone, address, DOB)
- Password change with current password verification
- User statistics and profile completion status

### 💰 Account Management
- Create new bank accounts (Savings, Checking)
- View all accounts for logged-in user
- View individual account details
- Delete/deactivate accounts (only if zero balance)
- Currency support (INR)

### 💸 Transaction Management
- Deposit money to account
- Withdraw money with balance validation
- Transfer money between accounts
- View complete transaction history
- Automatic transaction recording with timestamps

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core programming language |
| Spring Boot | 3.2.0 | Framework for building REST APIs |
| Spring Security | 3.2.0 | Authentication and authorization |
| Spring Data JPA | 3.2.0 | Database ORM |
| JWT (JJWT) | 0.11.5 | Token-based authentication |
| MySQL | 8.0+ | Relational database |
| Hibernate | 6.3.1 | ORM implementation |
| Maven | 3.8+ | Build automation |
| Railway | - | Cloud deployment platform |

---

## 📚 API Documentation

### Base URL
https://siddusbank-backend-production.up.railway.app/api


### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register a new user | ❌ No |
| POST | `/auth/login` | Login and get JWT token | ❌ No |
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
| POST | `/accounts/{id}/deposit` | Deposit money to account | ✅ Yes |
| POST | `/accounts/{id}/withdraw` | Withdraw money from account | ✅ Yes |
| POST | `/accounts/transfer` | Transfer money between accounts | ✅ Yes |
| DELETE | `/accounts/{id}` | Delete/deactivate account | ✅ Yes |
| GET | `/accounts/{id}/transactions` | Get transaction history | ✅ Yes |

---

## 🚀 Quick Test

### Test Backend Connection
curl https://siddusbank-backend-production.up.railway.app/api/auth/test

Register a User
curl -X POST https://siddusbank-backend-production.up.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123","fullName":"Test User"}'

Login
curl -X POST https://siddusbank-backend-production.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
