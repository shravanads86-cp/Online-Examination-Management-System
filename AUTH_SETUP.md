# Authentication System - Setup & Usage

## Database Setup

Run this SQL to create the `users` table (or run the full `database.sql`):

```sql
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);
```

## Access URLs

Base URL: `http://localhost:8080/SchoolExamSystem/`

| Page | URL |
|------|-----|
| Login | login.jsp |
| Register | register.jsp |
| Forgot Password | forgot.jsp |
| Reset Password | resetPassword.jsp |
| Dashboard | dashboard.jsp |

## Flow Summary

1. **Register** → If email exists: "User already exists" | Else: "Registration successful"
2. **Login** → Not registered: "User not registered" | Wrong password: "Wrong password" + Forgot Password link | Correct: Redirect to dashboard
3. **Forgot Password** → Enter email → If found: Reset page | If not: "User not registered"
4. **Reset Password** → New password + Confirm → If match: "Password reset successful"

## Features

- Unlimited login attempts (no lockout)
- No account blocking
- JDBC + PreparedStatement only
- No frameworks (plain Servlets + JSP)
