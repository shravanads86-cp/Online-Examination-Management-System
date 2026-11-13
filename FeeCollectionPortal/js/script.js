/**
 * Fee Collection Analysis Portal - Main JavaScript
 */

const API_BASE = 'php/';

function formatCurrency(amount) {
    return 'Rs. ' + parseFloat(amount || 0).toLocaleString('en-IN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

function showAlert(message, type, containerId) {
    const container = document.getElementById(containerId || 'alertBox');
    if (!container) return;

    container.className = 'alert alert-' + (type === 'error' ? 'error' : type === 'success' ? 'success' : 'info');
    container.textContent = message;
    container.classList.remove('hidden');

    setTimeout(function() {
        container.classList.add('hidden');
    }, 5000);
}

function getStatusBadge(status) {
    const cls = status === 'Paid' ? 'badge-paid' : status === 'Partial' ? 'badge-partial' : 'badge-pending';
    return '<span class="badge ' + cls + '">' + status + '</span>';
}

async function checkAuth() {
    try {
        const response = await fetch(API_BASE + 'checkSession.php');
        const data = await response.json();

        if (!data.logged_in) {
            window.location.href = 'login.html';
            return false;
        }

        const userEl = document.getElementById('adminUsername');
        if (userEl) userEl.textContent = data.username;
        return true;
    } catch (e) {
        window.location.href = 'login.html';
        return false;
    }
}

function setupSidebar() {
    const toggle = document.getElementById('menuToggle');
    const sidebar = document.getElementById('sidebar');

    if (toggle && sidebar) {
        toggle.addEventListener('click', function() {
            sidebar.classList.toggle('open');
        });
    }

    const currentPage = window.location.pathname.split('/').pop();
    document.querySelectorAll('.sidebar-nav a').forEach(function(link) {
        if (link.getAttribute('href') === currentPage) {
            link.classList.add('active');
        }
    });
}

async function logout() {
    if (!confirm('Are you sure you want to logout?')) return;

    try {
        const response = await fetch(API_BASE + 'logout.php');
        const data = await response.json();
        window.location.href = data.redirect || 'login.html';
    } catch (e) {
        window.location.href = 'login.html';
    }
}

function openModal(modalId) {
    document.getElementById(modalId).classList.add('show');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('show');
}

document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('show');
    }
});

/* Login Page */
async function handleLogin(e) {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);
    const btn = form.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.textContent = 'Logging in...';

    try {
        const response = await fetch(API_BASE + 'login.php', {
            method: 'POST',
            body: formData
        });
        const data = await response.json();

        if (data.success) {
            window.location.href = data.redirect || 'dashboard.html';
        } else {
            showAlert(data.message, 'error', 'loginAlert');
        }
    } catch (err) {
        showAlert('Connection error. Make sure XAMPP/WAMP is running.', 'error', 'loginAlert');
    }

    btn.disabled = false;
    btn.textContent = 'Login';
}

/* Change Password */
async function handleChangePassword(e) {
    e.preventDefault();
    const formData = new FormData(e.target);

    try {
        const response = await fetch(API_BASE + 'changePassword.php', {
            method: 'POST',
            body: formData
        });
        const data = await response.json();
        showAlert(data.message, data.success ? 'success' : 'error');
        if (data.success) {
            e.target.reset();
            closeModal('changePasswordModal');
        }
    } catch (err) {
        showAlert('Failed to change password', 'error');
    }
}

/* Dashboard */
async function loadDashboard() {
    try {
        const response = await fetch(API_BASE + 'dashboard.php');
        const data = await response.json();

        if (!data.success) return;

        document.getElementById('totalStudents').textContent = data.stats.total_students;
        document.getElementById('totalRevenue').textContent = formatCurrency(data.stats.total_revenue);
        document.getElementById('totalPending').textContent = formatCurrency(data.stats.total_pending);
        document.getElementById('totalDefaulters').textContent = data.stats.total_defaulters;

        const tbody = document.getElementById('recentPaymentsBody');
        if (!tbody) return;

        if (data.recent_payments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="empty-state">No recent payments</td></tr>';
            return;
        }

        tbody.innerHTML = data.recent_payments.map(function(p) {
            return '<tr>' +
                '<td>' + p.receipt_no + '</td>' +
                '<td>' + p.name + '</td>' +
                '<td>' + p.usn + '</td>' +
                '<td>' + formatCurrency(p.amount) + '</td>' +
                '<td>' + formatDate(p.payment_date) + '</td>' +
                '<td>' + p.payment_mode + '</td>' +
                '</tr>';
        }).join('');
    } catch (err) {
        showAlert('Failed to load dashboard data', 'error');
    }
}

/* Students */
async function loadStudents(search) {
    search = search || '';
    try {
        const response = await fetch(API_BASE + 'addStudent.php?search=' + encodeURIComponent(search));
        const data = await response.json();

        const tbody = document.getElementById('studentsTableBody');
        if (!data.success || !tbody) return;

        if (data.students.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="empty-state">No students found</td></tr>';
            return;
        }

        tbody.innerHTML = data.students.map(function(s) {
            return '<tr>' +
                '<td>' + s.usn + '</td>' +
                '<td>' + s.name + '</td>' +
                '<td>' + s.department + '</td>' +
                '<td>' + s.semester + '</td>' +
                '<td>' + (s.phone || '-') + '</td>' +
                '<td>' + formatCurrency(s.total_fee) + '</td>' +
                '<td>' + formatCurrency(s.paid_amount) + '</td>' +
                '<td>' + formatCurrency(s.pending_amount) + '</td>' +
                '<td>' + getStatusBadge(s.status || 'Pending') + '</td>' +
                '<td class="actions">' +
                    '<button class="btn btn-sm btn-secondary" onclick="editStudent(' + s.student_id + ')">Edit</button>' +
                    '<button class="btn btn-sm btn-danger" onclick="deleteStudent(' + s.student_id + ', \'' + s.name.replace(/'/g, "\\'") + '\')">Delete</button>' +
                '</td>' +
                '</tr>';
        }).join('');
    } catch (err) {
        showAlert('Failed to load students', 'error');
    }
}

async function handleAddStudent(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const editId = document.getElementById('editStudentId').value;

    try {
        let response;
        if (editId) {
            formData.append('student_id', editId);
            const params = new URLSearchParams(formData);
            response = await fetch(API_BASE + 'addStudent.php', {
                method: 'PUT',
                body: params
            });
        } else {
            response = await fetch(API_BASE + 'addStudent.php', {
                method: 'POST',
                body: formData
            });
        }

        const data = await response.json();
        showAlert(data.message, data.success ? 'success' : 'error');

        if (data.success) {
            e.target.reset();
            document.getElementById('editStudentId').value = '';
            document.getElementById('studentFormTitle').textContent = 'Add New Student';
            loadStudents();
        }
    } catch (err) {
        showAlert('Failed to save student', 'error');
    }
}

async function editStudent(id) {
    try {
        const response = await fetch(API_BASE + 'addStudent.php?search=');
        const data = await response.json();
        const student = data.students.find(function(s) { return s.student_id == id; });
        if (!student) return;

        document.getElementById('editStudentId').value = student.student_id;
        document.getElementById('studentName').value = student.name;
        document.getElementById('studentUsn').value = student.usn;
        document.getElementById('studentDepartment').value = student.department;
        document.getElementById('studentSemester').value = student.semester;
        document.getElementById('studentPhone').value = student.phone || '';
        document.getElementById('studentEmail').value = student.email || '';
        document.getElementById('studentTotalFee').value = student.total_fee || '';
        document.getElementById('studentDueDate').value = student.due_date || '';
        document.getElementById('studentFormTitle').textContent = 'Update Student';
        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (err) {
        showAlert('Failed to load student data', 'error');
    }
}

async function deleteStudent(id, name) {
    if (!confirm('Delete student "' + name + '"? This cannot be undone.')) return;

    try {
        const response = await fetch(API_BASE + 'addStudent.php', {
            method: 'DELETE',
            body: 'student_id=' + id
        });
        const data = await response.json();
        showAlert(data.message, data.success ? 'success' : 'error');
        if (data.success) loadStudents();
    } catch (err) {
        showAlert('Failed to delete student', 'error');
    }
}

function searchStudents() {
    const search = document.getElementById('studentSearch').value;
    loadStudents(search);
}

/* Payments */
async function loadStudentsForPayment() {
    try {
        const response = await fetch(API_BASE + 'addStudent.php');
        const data = await response.json();
        const select = document.getElementById('paymentStudent');
        if (!select || !data.success) return;

        select.innerHTML = '<option value="">-- Select Student --</option>';
        data.students.forEach(function(s) {
            if (parseFloat(s.pending_amount) > 0) {
                select.innerHTML += '<option value="' + s.student_id + '" data-pending="' + s.pending_amount + '">' +
                    s.name + ' (' + s.usn + ') - Pending: ' + formatCurrency(s.pending_amount) + '</option>';
            }
        });
    } catch (err) {
        showAlert('Failed to load students', 'error');
    }
}

function showPendingAmount() {
    const select = document.getElementById('paymentStudent');
    const pendingEl = document.getElementById('pendingAmountDisplay');
    if (!select || !pendingEl) return;

    const option = select.options[select.selectedIndex];
    const pending = option ? option.getAttribute('data-pending') : 0;
    pendingEl.textContent = formatCurrency(pending || 0);
}

async function handlePayment(e) {
    e.preventDefault();
    const formData = new FormData(e.target);

    try {
        const response = await fetch(API_BASE + 'payment.php', {
            method: 'POST',
            body: formData
        });
        const data = await response.json();
        showAlert(data.message, data.success ? 'success' : 'error');

        if (data.success) {
            e.target.reset();
            showReceipt(data.receipt);
            loadPaymentHistory();
            loadStudentsForPayment();
        }
    } catch (err) {
        showAlert('Failed to record payment', 'error');
    }
}

function showReceipt(receipt) {
    const box = document.getElementById('receiptContent');
    if (!box) return;

    box.innerHTML =
        '<div class="receipt-box">' +
        '<h3>FEE PAYMENT RECEIPT</h3>' +
        '<div class="receipt-row"><span>Receipt No:</span><span>' + receipt.receipt_no + '</span></div>' +
        '<div class="receipt-row"><span>Date:</span><span>' + formatDate(receipt.payment_date) + '</span></div>' +
        '<div class="receipt-row"><span>Student:</span><span>' + receipt.name + '</span></div>' +
        '<div class="receipt-row"><span>USN:</span><span>' + receipt.usn + '</span></div>' +
        '<div class="receipt-row"><span>Department:</span><span>' + receipt.department + '</span></div>' +
        '<div class="receipt-row"><span>Payment Mode:</span><span>' + receipt.payment_mode + '</span></div>' +
        '<div class="receipt-row total"><span>Amount Paid:</span><span>' + formatCurrency(receipt.amount) + '</span></div>' +
        '<div class="receipt-row"><span>Total Fee:</span><span>' + formatCurrency(receipt.total_fee) + '</span></div>' +
        '<div class="receipt-row"><span>Total Paid:</span><span>' + formatCurrency(receipt.paid_amount) + '</span></div>' +
        '<div class="receipt-row"><span>Pending:</span><span>' + formatCurrency(receipt.pending_amount) + '</span></div>' +
        '</div>';

    openModal('receiptModal');
}

async function loadPaymentHistory() {
    const search = document.getElementById('paymentSearch') ? document.getElementById('paymentSearch').value : '';
    try {
        const response = await fetch(API_BASE + 'payment.php?search=' + encodeURIComponent(search));
        const data = await response.json();
        const tbody = document.getElementById('paymentsTableBody');
        if (!tbody || !data.success) return;

        if (data.payments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state">No payments found</td></tr>';
            return;
        }

        tbody.innerHTML = data.payments.map(function(p) {
            return '<tr>' +
                '<td>' + p.receipt_no + '</td>' +
                '<td>' + p.name + '</td>' +
                '<td>' + p.usn + '</td>' +
                '<td>' + formatCurrency(p.amount) + '</td>' +
                '<td>' + formatDate(p.payment_date) + '</td>' +
                '<td>' + p.payment_mode + '</td>' +
                '<td><button class="btn btn-sm btn-outline" onclick="viewReceipt(' + p.payment_id + ')">Receipt</button></td>' +
                '</tr>';
        }).join('');
    } catch (err) {
        showAlert('Failed to load payments', 'error');
    }
}

async function viewReceipt(paymentId) {
    try {
        const response = await fetch(API_BASE + 'payment.php');
        const data = await response.json();
        const payment = data.payments.find(function(p) { return p.payment_id == paymentId; });
        if (payment) {
            const studentResponse = await fetch(API_BASE + 'addStudent.php?search=' + payment.usn);
            const studentData = await studentResponse.json();
            const student = studentData.students[0];
            if (student) {
                payment.total_fee = student.total_fee;
                payment.paid_amount = student.paid_amount;
                payment.pending_amount = student.pending_amount;
            }
            showReceipt(payment);
        }
    } catch (err) {
        showAlert('Failed to load receipt', 'error');
    }
}

/* Defaulters */
async function loadDefaulters() {
    const filter = document.getElementById('defaulterFilter') ? document.getElementById('defaulterFilter').value : 'all';
    const department = document.getElementById('defaulterDepartment') ? document.getElementById('defaulterDepartment').value : '';
    const semester = document.getElementById('defaulterSemester') ? document.getElementById('defaulterSemester').value : '';

    try {
        const url = API_BASE + 'defaulters.php?filter=' + filter + '&department=' + encodeURIComponent(department) + '&semester=' + semester;
        const response = await fetch(url);
        const data = await response.json();

        if (!data.success) return;

        document.getElementById('totalDefaulterCount').textContent = data.total_count;
        document.getElementById('totalDueAmount').textContent = formatCurrency(data.total_due);

        const tbody = document.getElementById('defaultersTableBody');
        if (data.defaulters.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="empty-state">No defaulters found</td></tr>';
        } else {
            tbody.innerHTML = data.defaulters.map(function(d) {
                const overdue = d.days_overdue > 0 ? d.days_overdue + ' days' : '-';
                return '<tr>' +
                    '<td>' + d.usn + '</td>' +
                    '<td>' + d.name + '</td>' +
                    '<td>' + d.department + '</td>' +
                    '<td>' + d.semester + '</td>' +
                    '<td>' + formatCurrency(d.total_fee) + '</td>' +
                    '<td>' + formatCurrency(d.paid_amount) + '</td>' +
                    '<td>' + formatCurrency(d.pending_amount) + '</td>' +
                    '<td>' + formatDate(d.due_date) + '</td>' +
                    '<td>' + overdue + '</td>' +
                    '</tr>';
            }).join('');
        }

        const deptBody = document.getElementById('deptDefaultersBody');
        if (deptBody) {
            deptBody.innerHTML = data.department_wise.map(function(d) {
                return '<tr><td>' + d.department + '</td><td>' + d.defaulter_count + '</td><td>' + formatCurrency(d.total_pending) + '</td></tr>';
            }).join('') || '<tr><td colspan="3" class="empty-state">No data</td></tr>';
        }

        const semBody = document.getElementById('semDefaultersBody');
        if (semBody) {
            semBody.innerHTML = data.semester_wise.map(function(d) {
                return '<tr><td>Semester ' + d.semester + '</td><td>' + d.defaulter_count + '</td><td>' + formatCurrency(d.total_pending) + '</td></tr>';
            }).join('') || '<tr><td colspan="3" class="empty-state">No data</td></tr>';
        }
    } catch (err) {
        showAlert('Failed to load defaulters', 'error');
    }
}

/* Reports */
let currentReportType = 'daily';

function setReportType(type) {
    currentReportType = type;
    document.querySelectorAll('.filter-tabs button').forEach(function(btn) {
        btn.classList.toggle('active', btn.dataset.type === type);
    });

    document.getElementById('dailyFilter').style.display = type === 'daily' ? 'block' : 'none';
    document.getElementById('monthlyFilter').style.display = type === 'monthly' ? 'block' : 'none';
    document.getElementById('yearlyFilter').style.display = type === 'yearly' ? 'block' : 'none';
    document.getElementById('deptFilter').style.display = type === 'department' ? 'block' : 'none';

    loadReport();
}

async function loadReport() {
    let url = API_BASE + 'report.php?type=' + currentReportType;

    if (currentReportType === 'daily') {
        url += '&date=' + document.getElementById('reportDate').value;
    } else if (currentReportType === 'monthly') {
        url += '&month=' + document.getElementById('reportMonth').value;
    } else if (currentReportType === 'yearly' || currentReportType === 'department') {
        url += '&year=' + document.getElementById('reportYear').value;
    }

    const dept = document.getElementById('reportDepartment');
    if (dept && dept.value && currentReportType !== 'department') {
        url += '&department=' + encodeURIComponent(dept.value);
    }

    try {
        const response = await fetch(url);
        const data = await response.json();

        if (!data.success) return;

        document.getElementById('reportTotalAmount').textContent = formatCurrency(data.summary.total_amount);
        document.getElementById('reportTotalCount').textContent = data.summary.total_payments;

        const thead = document.getElementById('reportTableHead');
        const tbody = document.getElementById('reportTableBody');

        if (currentReportType === 'department') {
            thead.innerHTML = '<tr><th>Department</th><th>Payments</th><th>Total Collected</th><th>Students</th></tr>';
            tbody.innerHTML = data.data.map(function(r) {
                return '<tr><td>' + r.department + '</td><td>' + r.payment_count + '</td><td>' + formatCurrency(r.total_collected) + '</td><td>' + r.student_count + '</td></tr>';
            }).join('') || '<tr><td colspan="4" class="empty-state">No data</td></tr>';
        } else {
            thead.innerHTML = '<tr><th>Receipt</th><th>Student</th><th>USN</th><th>Department</th><th>Amount</th><th>Date</th><th>Mode</th></tr>';
            tbody.innerHTML = data.data.map(function(r) {
                return '<tr><td>' + r.receipt_no + '</td><td>' + r.name + '</td><td>' + r.usn + '</td><td>' + r.department + '</td><td>' + formatCurrency(r.amount) + '</td><td>' + formatDate(r.payment_date) + '</td><td>' + r.payment_mode + '</td></tr>';
            }).join('') || '<tr><td colspan="7" class="empty-state">No data for selected period</td></tr>';
        }
    } catch (err) {
        showAlert('Failed to load report', 'error');
    }
}

function exportReport() {
    let url = API_BASE + 'report.php?type=' + currentReportType + '&export=csv';

    if (currentReportType === 'daily') {
        url += '&date=' + document.getElementById('reportDate').value;
    } else if (currentReportType === 'monthly') {
        url += '&month=' + document.getElementById('reportMonth').value;
    } else {
        url += '&year=' + document.getElementById('reportYear').value;
    }

    window.open(url, '_blank');
}

function printReceipt() {
    window.print();
}

document.addEventListener('DOMContentLoaded', function() {
    setupSidebar();

    const loginForm = document.getElementById('loginForm');
    if (loginForm) loginForm.addEventListener('submit', handleLogin);

    const changePasswordForm = document.getElementById('changePasswordForm');
    if (changePasswordForm) changePasswordForm.addEventListener('submit', handleChangePassword);

    const studentForm = document.getElementById('studentForm');
    if (studentForm) studentForm.addEventListener('submit', handleAddStudent);

    const paymentForm = document.getElementById('paymentForm');
    if (paymentForm) paymentForm.addEventListener('submit', handlePayment);
});
