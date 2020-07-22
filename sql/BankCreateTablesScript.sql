DROP TABLE IF EXISTS user_accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS account_status;
DROP TABLE IF EXISTS account_types;


CREATE TABLE user_roles 
(	role_id serial PRIMARY KEY,
	user_role varchar(30)
);

CREATE TABLE account_status
(	status_id serial PRIMARY KEY,
	account_status varchar(30)
);

CREATE TABLE account_types
(	type_id serial PRIMARY KEY,
	account_type varchar(30)
);

CREATE TABLE users
(	user_id serial PRIMARY KEY,
	username varchar(30),
	user_password varchar(30),
	first_name varchar(30),
	last_name varchar(30),
	email varchar(255),
	user_role integer REFERENCES user_roles(role_id)
);

CREATE TABLE accounts
(	account_id serial PRIMARY KEY,
	balance NUMERIC(10,2),
	status integer REFERENCES account_status(status_id),
	account_type integer REFERENCES account_types(type_id)
);

CREATE TABLE user_accounts
(	account_id integer REFERENCES accounts(account_id) ON DELETE CASCADE,
	user_id integer REFERENCES users(user_id) ON DELETE CASCADE,
	primary_user boolean DEFAULT TRUE,
	PRIMARY KEY (account_id, user_id)
);