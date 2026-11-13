<?php
/**
 * One-time setup script
 * Run once: http://localhost/FeeCollectionPortal/php/setup.php
 * Creates admin user with password: admin123
 */
require_once 'db.php';

$conn = new mysqli(DB_HOST, DB_USER, DB_PASS);

if ($conn->connect_error) {
    die('Connection failed: ' . $conn->connect_error);
}

$sqlFile = file_get_contents(__DIR__ . '/../database/fee_collection.sql');
$queries = array_filter(array_map('trim', explode(';', $sqlFile)));

foreach ($queries as $query) {
    if ($query !== '' && stripos($query, '--') !== 0) {
        if (!$conn->query($query)) {
            if (strpos($conn->error, 'Duplicate') === false) {
                echo "Warning: " . $conn->error . "<br>";
            }
        }
    }
}

$conn->select_db(DB_NAME);
$hashedPassword = password_hash('admin123', PASSWORD_DEFAULT);

$check = $conn->query("SELECT admin_id FROM admin WHERE username = 'admin'");
if ($check && $check->num_rows > 0) {
    $stmt = $conn->prepare("UPDATE admin SET password = ? WHERE username = 'admin'");
    $stmt->bind_param('s', $hashedPassword);
    $stmt->execute();
    echo "<h2>Setup Complete!</h2>";
    echo "<p>Admin password reset to: <strong>admin123</strong></p>";
} else {
    $stmt = $conn->prepare("INSERT INTO admin (username, password) VALUES ('admin', ?)");
    $stmt->bind_param('s', $hashedPassword);
    $stmt->execute();
    echo "<h2>Setup Complete!</h2>";
    echo "<p>Admin created - Username: <strong>admin</strong>, Password: <strong>admin123</strong></p>";
}

echo "<p><a href='../login.html'>Go to Login Page</a></p>";
echo "<p style='color:red;'>Delete or rename setup.php after setup for security.</p>";
