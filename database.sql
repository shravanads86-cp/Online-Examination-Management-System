-- School Exam System Database
-- Run this script in MySQL to create the database and tables

CREATE DATABASE IF NOT EXISTS schlexamsystem;
USE schlexamsystem;

-- Users table (Authentication System)
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Insert default admin (username: admin, password: admin)
INSERT INTO admin (username, password) VALUES ('admin', 'admin');

-- Student table
CREATE TABLE IF NOT EXISTS student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING'
);

-- Teacher table
CREATE TABLE IF NOT EXISTS teacher (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    subject VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING'
);

-- Exam table
CREATE TABLE IF NOT EXISTS exam (
    exam_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    subject VARCHAR(100),
    teacher_id INT,
    duration INT DEFAULT 30,
    FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

-- Question table
CREATE TABLE IF NOT EXISTS question (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500),
    option_b VARCHAR(500),
    option_c VARCHAR(500),
    option_d VARCHAR(500),
    correct_answer CHAR(1),
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id)
);

-- Result table
CREATE TABLE IF NOT EXISTS result (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    exam_id INT,
    score INT,
    total_questions INT,
    exam_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id)
);
