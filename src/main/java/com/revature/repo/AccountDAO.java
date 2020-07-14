package com.revature.repo;

import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.User;

public interface AccountDAO {

	public boolean insertAccount(Account account);
	public boolean updateAccount(Account account);
	public boolean deposit(double amt);
	public boolean withdraw(double amt);
	public double getBalance();
	public Account getAccountById(int id);
	public Set<Account> listAllAccounts();
	public boolean approveAccount();
	public boolean denyAccount();
	public boolean closeAccount();
	public AccountStatus getAccountStatus();
	public AccountType getAccountType();
	public User getAccountOwner();
	public Set<User> getAccountOwners();
	
}
