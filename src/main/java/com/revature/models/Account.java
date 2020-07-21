package com.revature.models;

import java.text.NumberFormat;

import com.revature.repo.AccountStatusDAO;
import com.revature.repo.AccountStatusDAOImpl;
import com.revature.repo.AccountTypeDAO;
import com.revature.repo.AccountTypeDAOImpl;

public class Account {
	  private int accountId; // primary key
	  private double balance;  // not null
	  private AccountStatus status;
	  private AccountType type;
	
	 public Account() {
	}

	 public Account(int accountId, double balance, AccountStatus status, AccountType type) {
		this.accountId = accountId;
		this.balance = balance;
		this.status = status;
		this.type = type;
	}
	 public Account(int accountId, double balance, int statusId, int typeId) {
		this.accountId = accountId;
		this.balance = balance;
		AccountStatusDAO asd = AccountStatusDAOImpl.getInstance();
		this.status = asd.getAccountStatusByID(statusId);
		AccountTypeDAO atd = AccountTypeDAOImpl.getInstance();
		this.type = atd.getAccountTypeById(typeId);
	 }
	public Account(double balance, AccountType type) {
		super();
		this.balance = balance;
		this.status = new AccountStatus("Pending");
		this.type = type;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public boolean isActive() {
		AccountStatus open = new AccountStatus(2);
		if (!status.equals(open)) {
			return false;
		} else {
			return true;
		}
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return "[Account ID: " + accountId + "] " + type + " [Balance: " + nf.format(balance) + "] " + status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountId != other.accountId)
			return false;
		return true;
	}


}