<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

$conn = getConnection();

$type = trim($_GET['type'] ?? 'daily');
$date = trim($_GET['date'] ?? date('Y-m-d'));
$month = trim($_GET['month'] ?? date('Y-m'));
$year = trim($_GET['year'] ?? date('Y'));
$department = trim($_GET['department'] ?? '');
$export = trim($_GET['export'] ?? '');

$data = [];
$summary = ['total_amount' => 0, 'total_payments' => 0];

switch ($type) {
    case 'daily':
        $sql = "SELECT p.*, s.name, s.usn, s.department
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE p.payment_date = ?";
        $params = [$date];
        $types = 's';

        if ($department !== '') {
            $sql .= " AND s.department = ?";
            $params[] = $department;
            $types .= 's';
        }
        $sql .= " ORDER BY p.payment_id ASC";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param($types, ...$params);
        $stmt->execute();
        $result = $stmt->get_result();
        break;

    case 'monthly':
        $sql = "SELECT p.*, s.name, s.usn, s.department
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE DATE_FORMAT(p.payment_date, '%Y-%m') = ?";
        $params = [$month];
        $types = 's';

        if ($department !== '') {
            $sql .= " AND s.department = ?";
            $params[] = $department;
            $types .= 's';
        }
        $sql .= " ORDER BY p.payment_date ASC, p.payment_id ASC";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param($types, ...$params);
        $stmt->execute();
        $result = $stmt->get_result();
        break;

    case 'yearly':
        $sql = "SELECT p.*, s.name, s.usn, s.department
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE YEAR(p.payment_date) = ?";
        $params = [$year];
        $types = 's';

        if ($department !== '') {
            $sql .= " AND s.department = ?";
            $params[] = $department;
            $types .= 's';
        }
        $sql .= " ORDER BY p.payment_date ASC, p.payment_id ASC";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param($types, ...$params);
        $stmt->execute();
        $result = $stmt->get_result();
        break;

    case 'department':
        $yearInt = intval($year);
        $sql = "SELECT s.department,
                COUNT(p.payment_id) AS payment_count,
                COALESCE(SUM(p.amount), 0) AS total_collected,
                COUNT(DISTINCT s.student_id) AS student_count
                FROM students s
                LEFT JOIN payments p ON s.student_id = p.student_id
                    AND YEAR(p.payment_date) = $yearInt
                GROUP BY s.department
                ORDER BY total_collected DESC";
        $result = $conn->query($sql);
        break;

    default:
        sendJson(['success' => false, 'message' => 'Invalid report type']);
}

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
    if ($type !== 'department') {
        $summary['total_amount'] += floatval($row['amount']);
        $summary['total_payments']++;
    } else {
        $summary['total_amount'] += floatval($row['total_collected']);
        $summary['total_payments'] += intval($row['payment_count']);
    }
}

if ($export === 'csv') {
    header('Content-Type: text/csv');
    header('Content-Disposition: attachment; filename="fee_report_' . $type . '_' . date('Ymd') . '.csv"');

    $output = fopen('php://output', 'w');

    if ($type === 'department') {
        fputcsv($output, ['Department', 'Payment Count', 'Total Collected', 'Student Count']);
        foreach ($data as $row) {
            fputcsv($output, [$row['department'], $row['payment_count'], $row['total_collected'], $row['student_count']]);
        }
    } else {
        fputcsv($output, ['Receipt No', 'Student Name', 'USN', 'Department', 'Amount', 'Date', 'Mode']);
        foreach ($data as $row) {
            fputcsv($output, [$row['receipt_no'], $row['name'], $row['usn'], $row['department'], $row['amount'], $row['payment_date'], $row['payment_mode']]);
        }
    }
    fclose($output);
    exit;
}

sendJson([
    'success' => true,
    'type' => $type,
    'data' => $data,
    'summary' => $summary
]);
