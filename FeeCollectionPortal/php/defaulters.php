<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

$conn = getConnection();

$filter = trim($_GET['filter'] ?? 'all');
$department = trim($_GET['department'] ?? '');
$semester = intval($_GET['semester'] ?? 0);

$sql = "SELECT s.student_id, s.name, s.usn, s.department, s.semester, s.phone, s.email,
        f.fee_id, f.total_fee, f.paid_amount, f.pending_amount, f.due_date, f.status,
        DATEDIFF(CURDATE(), f.due_date) AS days_overdue
        FROM students s
        JOIN fees f ON s.student_id = f.student_id
        WHERE f.pending_amount > 0";

if ($filter === 'overdue') {
    $sql .= " AND f.due_date < CURDATE()";
}

if ($department !== '') {
    $sql .= " AND s.department = '" . $conn->real_escape_string($department) . "'";
}

if ($semester > 0) {
    $sql .= " AND s.semester = $semester";
}

$sql .= " ORDER BY f.due_date ASC, f.pending_amount DESC";

$result = $conn->query($sql);
$defaulters = [];
$totalDue = 0;

while ($row = $result->fetch_assoc()) {
    $defaulters[] = $row;
    $totalDue += floatval($row['pending_amount']);
}

$deptSql = "SELECT s.department, COUNT(*) AS defaulter_count, SUM(f.pending_amount) AS total_pending
            FROM students s
            JOIN fees f ON s.student_id = f.student_id
            WHERE f.pending_amount > 0
            GROUP BY s.department
            ORDER BY total_pending DESC";
$deptResult = $conn->query($deptSql);
$deptWise = [];
while ($row = $deptResult->fetch_assoc()) {
    $deptWise[] = $row;
}

$semSql = "SELECT s.semester, COUNT(*) AS defaulter_count, SUM(f.pending_amount) AS total_pending
           FROM students s
           JOIN fees f ON s.student_id = f.student_id
           WHERE f.pending_amount > 0
           GROUP BY s.semester
           ORDER BY s.semester ASC";
$semResult = $conn->query($semSql);
$semWise = [];
while ($row = $semResult->fetch_assoc()) {
    $semWise[] = $row;
}

sendJson([
    'success' => true,
    'defaulters' => $defaulters,
    'total_due' => $totalDue,
    'total_count' => count($defaulters),
    'department_wise' => $deptWise,
    'semester_wise' => $semWise
]);
