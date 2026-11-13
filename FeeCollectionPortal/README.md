# Fee Collection Analysis Portal

**Payment Tracking, Defaulter Analysis and Revenue Reporting**

A simple college project built with **HTML, CSS, JavaScript, PHP, and MySQL** — no heavy frameworks.

---

## Project Title

**Fee Collection Analysis Portal with Payment Tracking, Defaulter Analysis and Revenue Reporting**

## Mini Abstract

The Fee Collection Analysis Portal is a web-based application developed to manage and monitor student fee payments efficiently. The system provides fee payment tracking, defaulter analysis, and revenue reporting features. Administrators can maintain student records, record fee payments, identify fee defaulters, and generate collection reports. The system improves accuracy, reduces manual work, and provides real-time insights into institutional revenue and pending fee collections.

---

## Modules

| Module | Features |
|--------|----------|
| **Admin Login** | Secure login, change password, dashboard access |
| **Student Management** | Add, update, delete, search students |
| **Fee Payment Tracking** | Record payments, view history, check paid/pending, generate receipt |
| **Defaulter Analysis** | List defaulters, due calculation, due date tracking, dept/semester-wise |
| **Revenue Reports** | Daily, monthly, yearly, department-wise reports, CSV export |
| **Dashboard** | Total students, revenue, pending fees, defaulters, recent payments |

---

## Tech Stack

- **Frontend:** HTML, CSS, JavaScript
- **Backend:** PHP
- **Database:** MySQL

---

## Setup Instructions (XAMPP / WAMP)

### Step 1: Install XAMPP
Download and install [XAMPP](https://www.apachefriends.org/) (includes Apache, PHP, MySQL).

### Step 2: Copy Project
Copy the `FeeCollectionPortal` folder to:
- **XAMPP:** `C:\xampp\htdocs\FeeCollectionPortal`
- **WAMP:** `C:\wamp64\www\FeeCollectionPortal`

### Step 3: Start Services
Open XAMPP Control Panel and start **Apache** and **MySQL**.

### Step 4: Import Database
Open browser: `http://localhost/phpmyadmin`

1. Click **Import**
2. Choose file: `database/fee_collection.sql`
3. Click **Go**

### Step 5: Create Admin User
Open in browser:
```
http://localhost/FeeCollectionPortal/php/setup.php
```

This creates the admin account. **Delete `setup.php` after setup.**

### Step 6: Login
Open:
```
http://localhost/FeeCollectionPortal/login.html
```

**Default Login:**
- Username: `admin`
- Password: `admin123`

---

## Database Configuration

Edit `php/db.php` if your MySQL settings differ:

```php
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');        // Your MySQL password
define('DB_NAME', 'fee_collection');
```

---

## Viva Points (Easy to Explain)

1. **Frontend** sends requests using JavaScript `fetch()` API
2. **Backend** PHP files handle CRUD operations and return JSON
3. **MySQL** stores all data with foreign key relationships
4. **Session-based login** keeps admin authenticated
5. **Password hashing** using PHP `password_hash()` and `password_verify()`
6. **Defaulter logic:** Students with `pending_amount > 0`
7. **Reports:** SQL queries filter payments by date/month/year
