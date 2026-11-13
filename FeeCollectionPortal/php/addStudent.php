<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

$conn = getConnection();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $search = trim($_GET['search'] ?? '');
    $department = trim($_GET['department'] ?? '');

    $sql = "SELECT s.*, f.fee_id, f.total_fee, f.paid_amount, f.pending_amount, f.due_date, f.status
            FROM students s
            LEFT JOIN fees f ON s.student_id = f.student_id
            WHERE 1=1";
    $params = [];
    $types = '';

    if ($search !== '') {
        $sql .= " AND (s.name LIKE ? OR s.usn LIKE ? OR s.email LIKE ? OR s.phone LIKE ?)";
        $searchTerm = '%' . $search . '%';
        $params = array_merge($params, [$searchTerm, $searchTerm, $searchTerm, $searchTerm]);
        $types .= 'ssss';
    }

    if ($department !== '') {
        $sql .= " AND s.department = ?";
        $params[] = $department;
        $types .= 's';
    }

    $sql .= " ORDER BY s.student_id DESC";

    $stmt = $conn->prepare($sql);
    if (!empty($params)) {
        $stmt->bind_param($types, ...$params);
    }
    $stmt->execute();
    $result = $stmt->get_result();
    $students = [];
    while ($row = $result->fetch_assoc()) {
        $students[] = $row;
    }

    sendJson(['success' => true, 'students' => $students]);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = trim($_POST['name'] ?? '');
    $usn = trim($_POST['usn'] ?? '');
    $department = trim($_POST['department'] ?? '');
    $semester = intval($_POST['semester'] ?? 0);
    $phone = trim($_POST['phone'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $totalFee = floatval($_POST['total_fee'] ?? 0);
    $dueDate = trim($_POST['due_date'] ?? '');

    if ($name === '' || $usn === '' || $department === '' || $semester <= 0 || $totalFee <= 0 || $dueDate === '') {
        sendJson(['success' => false, 'message' => 'Please fill all required fields']);
    }

    $checkStmt = $conn->prepare('SELECT student_id FROM students WHERE usn = ?');
    $checkStmt->bind_param('s', $usn);
    $checkStmt->execute();
    if ($checkStmt->get_result()->num_rows > 0) {
        sendJson(['success' => false, 'message' => 'USN already exists']);
    }

    $conn->begin_transaction();
    try {
        $stmt = $conn->prepare('INSERT INTO students (name, usn, department, semester, phone, email) VALUES (?, ?, ?, ?, ?, ?)');
        $stmt->bind_param('sssiss', $name, $usn, $department, $semester, $phone, $email);
        $stmt->execute();
        $studentId = $conn->insert_id;

        $feeStmt = $conn->prepare('INSERT INTO fees (student_id, total_fee, paid_amount, pending_amount, due_date, status) VALUES (?, ?, 0, ?, ?, ?)');
        $status = 'Pending';
        $feeStmt->bind_param('iddss', $studentId, $totalFee, $totalFee, $dueDate, $status);
        $feeStmt->execute();

        $conn->commit();
        sendJson(['success' => true, 'message' => 'Student added successfully']);
    } catch (Exception $e) {
        $conn->rollback();
        sendJson(['success' => false, 'message' => 'Failed to add student: ' . $e->getMessage()]);
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    parse_str(file_get_contents('php://input'), $_PUT);
    $studentId = intval($_PUT['student_id'] ?? 0);
    $name = trim($_PUT['name'] ?? '');
    $usn = trim($_PUT['usn'] ?? '');
    $department = trim($_PUT['department'] ?? '');
    $semester = intval($_PUT['semester'] ?? 0);
    $phone = trim($_PUT['phone'] ?? '');
    $email = trim($_PUT['email'] ?? '');
    $totalFee = floatval($_PUT['total_fee'] ?? 0);
    $dueDate = trim($_PUT['due_date'] ?? '');

    if ($studentId <= 0 || $name === '' || $usn === '') {
        sendJson(['success' => false, 'message' => 'Invalid student data']);
    }

    $checkStmt = $conn->prepare('SELECT student_id FROM students WHERE usn = ? AND student_id != ?');
    $checkStmt->bind_param('si', $usn, $studentId);
    $checkStmt->execute();
    if ($checkStmt->get_result()->num_rows > 0) {
        sendJson(['success' => false, 'message' => 'USN already exists for another student']);
    }

    $conn->begin_transaction();
    try {
        $stmt = $conn->prepare('UPDATE students SET name=?, usn=?, department=?, semester=?, phone=?, email=? WHERE student_id=?');
        $stmt->bind_param('sssissi', $name, $usn, $department, $semester, $phone, $email, $studentId);
        $stmt->execute();

        if ($totalFee > 0 && $dueDate !== '') {
            $feeResult = $conn->query("SELECT paid_amount FROM fees WHERE student_id = $studentId LIMIT 1");
            $paidAmount = 0;
            if ($feeResult && $feeResult->num_rows > 0) {
                $paidAmount = floatval($feeResult->fetch_assoc()['paid_amount']);
            }
            $pending = max(0, $totalFee - $paidAmount);
            $status = $pending <= 0 ? 'Paid' : ($paidAmount > 0 ? 'Partial' : 'Pending');

            $feeStmt = $conn->prepare('UPDATE fees SET total_fee=?, pending_amount=?, due_date=?, status=? WHERE student_id=?');
            $feeStmt->bind_param('ddssi', $totalFee, $pending, $dueDate, $status, $studentId);
            $feeStmt->execute();
        }

        $conn->commit();
        sendJson(['success' => true, 'message' => 'Student updated successfully']);
    } catch (Exception $e) {
        $conn->rollback();
        sendJson(['success' => false, 'message' => 'Failed to update student']);
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    parse_str(file_get_contents('php://input'), $_DELETE);
    $studentId = intval($_DELETE['student_id'] ?? 0);

    if ($studentId <= 0) {
        sendJson(['success' => false, 'message' => 'Invalid student ID']);
    }

    $stmt = $conn->prepare('DELETE FROM students WHERE student_id = ?');
    $stmt->bind_param('i', $studentId);

    if ($stmt->execute()) {
        sendJson(['success' => true, 'message' => 'Student deleted successfully']);
    } else {
        sendJson(['success' => false, 'message' => 'Failed to delete student']);
    }
}

sendJson(['success' => false, 'message' => 'Invalid request']);
