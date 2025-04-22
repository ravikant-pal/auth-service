# 🔐 Auth Service - Spring Boot JWT + OTP Authentication

A secure, extensible Spring Boot microservice for handling:

- JWT-based login and token management
- Real-time OTP-based authentication via **SMS**, **Email**, and **WhatsApp**
- Annotation-based role and permission access control
- Stateless, scalable, and easy to integrate across microservices

---

## 📌 Overview

This **Auth Service** supports both **JWT-based login** and **OTP-based authentication** for users across various channels including:

- 📱 SMS (Text Message)
- 📧 Email
- 💬 WhatsApp

OTP verification is centralized under one API, making it easy to use across frontend apps and services.

It also uses custom annotations (`@HasAnyRole`, `@HasAnyPermission`) and security filters from the shared `commons-library` for role/permission-based access control at the controller level.

---

## 🚀 Key Features

✅ Real-time OTP delivery over **SMS**, **Email**, and **WhatsApp**  
✅ Unified OTP send and verify endpoints  
✅ JWT token generation and refresh  
✅ Role & permission-based authorization using annotations  
✅ Stateless security with centralized filter from `commons-library`  
✅ Logout support for token/session invalidation  
✅ Easy integration with any frontend or mobile client

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security
- JWT (io.jsonwebtoken)
- PostgreSQL / Redis (for OTP or token storage)
- Twilio, SendGrid, WhatsApp API (or pluggable providers)
- Maven or Gradle
- Docker

---

## 📦 Installation

```bash
git clone https://github.com/ravikant-pal/auth-service.git
cd auth-service
./mvnw spring-boot:run
```

Configure OTP providers and secrets in `application.yml`.

---

## 📡 OTP-Based Authentication

This service supports OTP-based login/verification across channels.  
The **channel is determined automatically based on the user record or passed in request**.

### 🔁 API Endpoints

| Method   | Endpoint                        | Description                              |
|----------|----------------------------------|------------------------------------------|
| `GET`    | `/api/v1/auth/send-otp`         | Sends OTP to the user (SMS, Email, WhatsApp) |
| `POST`   | `/api/v1/auth/verify-otp`       | Verifies the OTP and logs the user in    |
| `PUT`    | `/api/v1/auth/refresh`          | Refreshes expired JWT token              |
| `DELETE` | `/api/v1/auth/logout`           | Logs out user and invalidates the token  |

### 📥 OTP Request Example

```http
GET /api/v1/auth/send-otp?identifier=ravikant@example.com
```

### 📤 OTP Verification Example

```json
POST /api/v1/auth/verify-otp
{
  "identifier": "ravikant@example.com",
  "otp": "123456"
}
```

- `identifier` can be an email, phone number, or WhatsApp ID depending on setup.
- On successful verification, a **JWT token** is returned for authenticated access.

---

## 🧪 JWT-Based Access Control

Post verification, users can access secured endpoints using the JWT token.

Use the token in headers:

```http
Authorization: Bearer <token>
```

---

## 🔐 Annotations for Access Control

Provided by `commons-library`, these annotations let you protect endpoints easily:

### ✅ `@HasAnyRole`

```java
@HasAnyRole("ADMIN")
@GetMapping("/admin/data")
public ResponseEntity<?> getAdminData() {
    return ResponseEntity.ok("Secret admin data");
}
```

### ✅ `@HasAnyPermission`

```java
@HasAnyPermission({"USER_READ", "USER_WRITE"})
@GetMapping("/users")
public List<UserDto> getUsers() {
    return userService.getAllUsers();
}
```

---

## 📑 Sample Flow

```plaintext
1. User opens app → enters phone/email/WhatsApp
2. Client sends `GET /send-otp?identifier=xxx`
3. User receives OTP
4. Client sends `POST /verify-otp` with code
5. Server responds with JWT
6. Client uses JWT for further authenticated access
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 🛡️ License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file.

---

## 👨‍💻 Author

**Ravikant Pal**  
Backend Developer | Spring Security | Microservices  
[LinkedIn](https://linkedin.com/in/ravikant-pal) • [GitHub](https://github.com/ravikant-pal)

---

## 🌟 Feedback / Contributions

Open an issue or submit a PR at  
👉 [https://github.com/ravikant-pal/auth-service](https://github.com/ravikant-pal/auth-service)
