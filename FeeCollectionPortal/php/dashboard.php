<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

$conn = getConnection();

$sql = "SELECT 
    (SELECT COUNT(*) FROM students) AS total_students,
    (SELECT COALESCE(SUM(paid_amount), 0) FROM fees) AS total_revenue,
    (SELECT COALESCE(SUM(pending_amount), 0) FROM fees) AS total_pending,
    (SELECT COUNT(*) FROM fees WHERE status IN ('Pending', 'Partial') AND pending_amount > 0) AS total_defaulters";

$result = $conn->query($sql);
$stats = $result->fetch_assoc();

$recentSql = "SELECT p.payment_id, p.amount, p.payment_date, p.payment_mode, p.receipt_no,
              s.name, s.usn, s.department
              FROM payments p
              JOIN students s ON p.student_id = s.student_id
              ORDER BY p.payment_date DESC, p.payment_id DESC
              LIMIT 5";
$recentResult = $conn->query($recentSql);
$recentPayments = [];

while ($row = $recentResult->fetch_assoc()) {
    $recentPayments[] = $row;
}

sendJson([
    'success' => true,
    'stats' => $stats,
    'recent_payments' => $recentPayments
]);
