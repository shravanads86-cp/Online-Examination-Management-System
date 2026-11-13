<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendJson(['success' => false, 'message' => 'Invalid request method']);
}

$currentPassword = $_POST['current_password'] ?? '';
$newPassword = $_POST['new_password'] ?? '';
$confirmPassword = $_POST['confirm_password'] ?? '';

if ($currentPassword === '' || $newPassword === '' || $confirmPassword === '') {
    sendJson(['success' => false, 'message' => 'All fields are required']);
}

if (strlen($newPassword) < 6) {
    sendJson(['success' => false, 'message' => 'New password must be at least 6 characters']);
}

if ($newPassword !== $confirmPassword) {
    sendJson(['success' => false, 'message' => 'New passwords do not match']);
}

$conn = getConnection();
$adminId = getAdminId();
$stmt = $conn->prepare('SELECT password FROM admin WHERE admin_id = ?');
$stmt->bind_param('i', $adminId);
$stmt->execute();
$result = $stmt->get_result();
$admin = $result->fetch_assoc();

if (!password_verify($currentPassword, $admin['password'])) {
    sendJson(['success' => false, 'message' => 'Current password is incorrect']);
}

$hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);
$updateStmt = $conn->prepare('UPDATE admin SET password = ? WHERE admin_id = ?');
$updateStmt->bind_param('si', $hashedPassword, $adminId);

if ($updateStmt->execute()) {
    sendJson(['success' => true, 'message' => 'Password changed successfully']);
} else {
    sendJson(['success' => false, 'message' => 'Failed to change password']);
}
