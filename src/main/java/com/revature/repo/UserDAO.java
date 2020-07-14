package com.revature.repo;

import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountType;
import com.revature.models.User;

public interface UserDAO {

	public boolean insertUser(User user);
	public boolean updateUser(User user);
	public boolean deleteUser(User user);
	public User getUserById(int id);
	public Set<User> getAllUsers();
	public Account openNewAccount(double balance, AccountType type, User user);
	public Set<Account> getAllAccounts(User user);
	public int getNewestUserID();
}
