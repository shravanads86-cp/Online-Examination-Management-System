<?php
session_start();

function isLoggedIn() {
    return isset($_SESSION['admin_id']) && isset($_SESSION['admin_username']);
}

function requireLogin() {
    if (!isLoggedIn()) {
        header('Content-Type: application/json');
        echo json_encode(['success' => false, 'message' => 'Please login first', 'redirect' => 'login.html']);
        exit;
    }
}

function getAdminId() {
    return $_SESSION['admin_id'];
}

function getAdminUsername() {
    return $_SESSION['admin_username'];
}
