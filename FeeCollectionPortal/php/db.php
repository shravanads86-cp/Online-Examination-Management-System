<?php
/**
 * Database connection configuration
 * Update these values as per your XAMPP/WAMP setup
 */
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'fee_collection');

function getConnection() {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    if ($conn->connect_error) {
        die(json_encode(['success' => false, 'message' => 'Database connection failed: ' . $conn->connect_error]));
    }
    $conn->set_charset('utf8mb4');
    return $conn;
}

function sendJson($data) {
    header('Content-Type: application/json');
    echo json_encode($data);
    exit;
}

function sanitize($conn, $value) {
    return $conn->real_escape_string(trim($value));
}
