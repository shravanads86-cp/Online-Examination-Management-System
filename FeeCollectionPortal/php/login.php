<?php
require_once 'db.php';
require_once 'auth.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendJson(['success' => false, 'message' => 'Invalid request method']);
}

$username = trim($_POST['username'] ?? '');
$password = $_POST['password'] ?? '';

if ($username === '' || $password === '') {
    sendJson(['success' => false, 'message' => 'Username and password are required']);
}

$conn = getConnection();
$stmt = $conn->prepare('SELECT admin_id, username, password FROM admin WHERE username = ?');
$stmt->bind_param('s', $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    sendJson(['success' => false, 'message' => 'Invalid username or password']);
}

$admin = $result->fetch_assoc();

if (!password_verify($password, $admin['password'])) {
    sendJson(['success' => false, 'message' => 'Invalid username or password']);
}

$_SESSION['admin_id'] = $admin['admin_id'];
$_SESSION['admin_username'] = $admin['username'];

sendJson(['success' => true, 'message' => 'Login successful', 'redirect' => 'dashboard.html']);
