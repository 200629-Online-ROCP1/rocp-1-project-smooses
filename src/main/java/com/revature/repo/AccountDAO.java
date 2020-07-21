package com.revature.repo;

import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.User;

public interface AccountDAO {

	public boolean insertAccount(Account account);
	public boolean updateAccount(Account account);
	public boolean deposit(Account account, double amt);
	public boolean withdraw(Account account, double amt);
	public Account getAccountById(int id);
	public Set<Account> listAllAccounts();
	public boolean approveAccount(Account account);
	public boolean denyAccount(Account account);
	public boolean closeAccount(Account account);
	public AccountStatus getAccountStatus(Account account);
	public AccountType getAccountType(Account account);
	public User getAccountOwner(Account account);
	public Set<User> getAccountOwners(Account account);
	
}
