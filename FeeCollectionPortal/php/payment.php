<?php
require_once 'db.php';
require_once 'auth.php';
requireLogin();

$conn = getConnection();

function updateFeeStatus($conn, $studentId) {
    $result = $conn->query("SELECT total_fee, paid_amount, pending_amount FROM fees WHERE student_id = $studentId LIMIT 1");
    if (!$result || $result->num_rows === 0) return;

    $fee = $result->fetch_assoc();
    $totalFee = floatval($fee['total_fee']);
    $paidResult = $conn->query("SELECT COALESCE(SUM(amount), 0) AS total_paid FROM payments WHERE student_id = $studentId");
    $paidAmount = floatval($paidResult->fetch_assoc()['total_paid']);
    $pending = max(0, $totalFee - $paidAmount);

    if ($pending <= 0) {
        $status = 'Paid';
        $pending = 0;
        $paidAmount = $totalFee;
    } elseif ($paidAmount > 0) {
        $status = 'Partial';
    } else {
        $status = 'Pending';
    }

    $stmt = $conn->prepare('UPDATE fees SET paid_amount=?, pending_amount=?, status=? WHERE student_id=?');
    $stmt->bind_param('ddsi', $paidAmount, $pending, $status, $studentId);
    $stmt->execute();
}

function generateReceiptNo($conn) {
    $year = date('Y');
    $result = $conn->query("SELECT receipt_no FROM payments WHERE receipt_no LIKE 'RCP-$year-%' ORDER BY payment_id DESC LIMIT 1");
    $nextNum = 1;
    if ($result && $result->num_rows > 0) {
        $last = $result->fetch_assoc()['receipt_no'];
        $parts = explode('-', $last);
        $nextNum = intval(end($parts)) + 1;
    }
    return 'RCP-' . $year . '-' . str_pad($nextNum, 4, '0', STR_PAD_LEFT);
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $studentId = intval($_GET['student_id'] ?? 0);
    $search = trim($_GET['search'] ?? '');

    if ($studentId > 0) {
        $sql = "SELECT p.*, s.name, s.usn, s.department
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE p.student_id = ?
                ORDER BY p.payment_date DESC, p.payment_id DESC";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param('i', $studentId);
        $stmt->execute();
        $result = $stmt->get_result();
    } else {
        $sql = "SELECT p.*, s.name, s.usn, s.department
                FROM payments p
                JOIN students s ON p.student_id = s.student_id";
        if ($search !== '') {
            $sql .= " WHERE s.name LIKE ? OR s.usn LIKE ? OR p.receipt_no LIKE ?";
        }
        $sql .= " ORDER BY p.payment_date DESC, p.payment_id DESC LIMIT 100";

        if ($search !== '') {
            $searchTerm = '%' . $search . '%';
            $stmt = $conn->prepare($sql);
            $stmt->bind_param('sss', $searchTerm, $searchTerm, $searchTerm);
            $stmt->execute();
            $result = $stmt->get_result();
        } else {
            $result = $conn->query($sql);
        }
    }

    $payments = [];
    while ($row = $result->fetch_assoc()) {
        $payments[] = $row;
    }

    sendJson(['success' => true, 'payments' => $payments]);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $studentId = intval($_POST['student_id'] ?? 0);
    $amount = floatval($_POST['amount'] ?? 0);
    $paymentDate = trim($_POST['payment_date'] ?? date('Y-m-d'));
    $paymentMode = trim($_POST['payment_mode'] ?? 'Cash');

    if ($studentId <= 0 || $amount <= 0) {
        sendJson(['success' => false, 'message' => 'Please select student and enter valid amount']);
    }

    $feeResult = $conn->query("SELECT pending_amount FROM fees WHERE student_id = $studentId LIMIT 1");
    if (!$feeResult || $feeResult->num_rows === 0) {
        sendJson(['success' => false, 'message' => 'No fee record found for this student']);
    }

    $pending = floatval($feeResult->fetch_assoc()['pending_amount']);
    if ($amount > $pending) {
        sendJson(['success' => false, 'message' => 'Payment amount exceeds pending fee (Rs. ' . number_format($pending, 2) . ')']);
    }

    $receiptNo = generateReceiptNo($conn);
    $validModes = ['Cash', 'UPI', 'Card', 'Cheque', 'Online'];
    if (!in_array($paymentMode, $validModes)) {
        $paymentMode = 'Cash';
    }

    $conn->begin_transaction();
    try {
        $stmt = $conn->prepare('INSERT INTO payments (student_id, amount, payment_date, payment_mode, receipt_no) VALUES (?, ?, ?, ?, ?)');
        $stmt->bind_param('idsss', $studentId, $amount, $paymentDate, $paymentMode, $receiptNo);
        $stmt->execute();
        $paymentId = $conn->insert_id;

        updateFeeStatus($conn, $studentId);
        $conn->commit();

        $receiptSql = "SELECT p.*, s.name, s.usn, s.department, s.semester,
                       f.total_fee, f.paid_amount, f.pending_amount
                       FROM payments p
                       JOIN students s ON p.student_id = s.student_id
                       JOIN fees f ON s.student_id = f.student_id
                       WHERE p.payment_id = ?";
        $receiptStmt = $conn->prepare($receiptSql);
        $receiptStmt->bind_param('i', $paymentId);
        $receiptStmt->execute();
        $receipt = $receiptStmt->get_result()->fetch_assoc();

        sendJson(['success' => true, 'message' => 'Payment recorded successfully', 'receipt' => $receipt]);
    } catch (Exception $e) {
        $conn->rollback();
        sendJson(['success' => false, 'message' => 'Failed to record payment']);
    }
}

sendJson(['success' => false, 'message' => 'Invalid request']);
