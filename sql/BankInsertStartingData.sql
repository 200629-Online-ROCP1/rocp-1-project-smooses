INSERT INTO account_status (account_status)
VALUES ('Pending'), ('Open'), ('Closed'), ('Denied');

INSERT INTO account_types (account_type)
VALUES ('Checking'), ('Savings');

INSERT INTO user_roles (user_role)
VALUES ('Administrator'), ('Employee'), ('Standard'), ('Premium'); 

INSERT INTO users (username, user_password, first_name, last_name, email, user_role)
VALUES ('admin', 'adminpassword', 'Primary', 'Administrator', 'webadmin@bank.com', 1),
('employee', 'employeepassword', 'Helpful', 'Employee', 'h.employee@bank.com', 2),
('username', 'password', 'Stan', 'Dard', 'stan.dard@gmail.com', 3),
('vipuser', 'vipassword', 'Mr.', 'VIP', 'vip@viemail.com', 4),
('anotheruser', '1PASS2word3', 'Ann', 'Other', 'ann.other@yahoo.com', 3);

INSERT INTO accounts (balance, status, account_type)
VALUES (0, 3, 1), (100, 2, 1), (150, 2, 2), (0, 4, 2), (5000, 2, 2), (2500, 1, 1), (250, 2, 2);

INSERT INTO user_accounts (account_id, user_id)
VALUES (1, 3), (2, 3), (3, 3), (4, 3), (5, 4), (6, 4), (7,5);
