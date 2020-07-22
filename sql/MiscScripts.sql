--Returns newest account ID
SELECT MAX(account_id) FROM accounts;

--Returns newest user ID
SELECT MAX(user_id) FROM users;

--Shows All Accounts for Specified Owner
SELECT accounts.account_id, accounts.balance, accounts.account_type, accounts.status FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
WHERE users.user_id = 3;

--Shows All Owners for specified Account
SELECT users.user_id, users.username, users.user_password, users.first_name, users.last_name, users.email, users.user_role 
FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
WHERE accounts.account_id = 6;

--Shows All Accounts, associated Account Owner Name, and Status/Type Value instead of ID 
SELECT accounts.account_id, (users.first_name, users.last_name) AS account_owner, 
accounts.balance, account_types.account_type , account_status.account_status 
FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
JOIN account_types ON accounts.account_type = account_types.type_id 
JOIN account_status ON accounts.status = account_status.status_id;

--Interest Accrual
UPDATE accounts SET balance = balance + balance * .01 WHERE account_type = 2 AND status = 2;