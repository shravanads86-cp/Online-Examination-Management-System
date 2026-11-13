-- Fee Collection Analysis Portal - Database Setup
-- Run: mysql -u root -p < fee_collection.sql

CREATE DATABASE IF NOT EXISTS fee_collection;
USE fee_collection;

-- Admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    usn VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100)
);

-- Fees table (one record per student per semester/academic year)
CREATE TABLE IF NOT EXISTS fees (
    fee_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    total_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    pending_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    due_date DATE NOT NULL,
    status ENUM('Paid', 'Partial', 'Pending') DEFAULT 'Pending',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_mode ENUM('Cash', 'UPI', 'Card', 'Cheque', 'Online') DEFAULT 'Cash',
    receipt_no VARCHAR(30) NOT NULL UNIQUE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Admin user is created by php/setup.php (username: admin, password: admin123)

-- Sample students
INSERT INTO students (name, usn, department, semester, phone, email) VALUES
('Rahul Kumar', '1CS21CS001', 'Computer Science', 5, '9876543210', 'rahul@college.edu'),
('Priya Sharma', '1CS21CS002', 'Computer Science', 5, '9876543211', 'priya@college.edu'),
('Amit Patel', '1EC21EC001', 'Electronics', 3, '9876543212', 'amit@college.edu'),
('Sneha Reddy', '1ME21ME001', 'Mechanical', 7, '9876543213', 'sneha@college.edu'),
('Vikram Singh', '1CS21CS003', 'Computer Science', 3, '9876543214', 'vikram@college.edu');

-- Sample fees
INSERT INTO fees (student_id, total_fee, paid_amount, pending_amount, due_date, status) VALUES
(1, 50000.00, 50000.00, 0.00, '2025-06-30', 'Paid'),
(2, 50000.00, 25000.00, 25000.00, '2025-03-15', 'Partial'),
(3, 45000.00, 0.00, 45000.00, '2025-02-01', 'Pending'),
(4, 48000.00, 48000.00, 0.00, '2025-06-30', 'Paid'),
(5, 50000.00, 10000.00, 40000.00, '2025-01-10', 'Partial');

-- Sample payments
INSERT INTO payments (student_id, amount, payment_date, payment_mode, receipt_no) VALUES
(1, 30000.00, '2025-01-15', 'UPI', 'RCP-2025-0001'),
(1, 20000.00, '2025-03-20', 'Cash', 'RCP-2025-0002'),
(2, 25000.00, '2025-02-10', 'Online', 'RCP-2025-0003'),
(4, 48000.00, '2025-04-05', 'Card', 'RCP-2025-0004'),
(5, 10000.00, '2024-12-20', 'Cash', 'RCP-2025-0005');
