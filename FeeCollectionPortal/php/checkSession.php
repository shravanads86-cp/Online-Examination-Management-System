<?php
require_once 'db.php';
require_once 'auth.php';

if (isLoggedIn()) {
    sendJson(['success' => true, 'logged_in' => true, 'username' => getAdminUsername()]);
} else {
    sendJson(['success' => true, 'logged_in' => false]);
}
