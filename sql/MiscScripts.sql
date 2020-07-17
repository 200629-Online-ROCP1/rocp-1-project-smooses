SELECT MAX(account_id) FROM accounts;

DELETE FROM public.users
WHERE user_id = 12;

SELECT MAX(user_id) FROM users;

SELECT accounts.account_id, accounts.balance, accounts.account_type, accounts.status FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
WHERE users.user_id = 3;

SELECT users.user_id, users.username, users.user_password, users.first_name, users.last_name, users.email, users.user_role 
FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
WHERE accounts.account_id = 2;

SELECT accounts.account_id, (users.first_name, users.last_name) AS account_owner, 
accounts.balance, account_types.account_type , account_status.account_status 
FROM user_accounts 
JOIN users ON user_accounts.user_id = users.user_id 
JOIN accounts ON user_accounts.account_id = accounts.account_id
JOIN account_types ON accounts.account_type = account_types.type_id 
JOIN account_status ON accounts.status = account_status.status_id;