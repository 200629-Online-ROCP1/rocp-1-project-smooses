package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO{
	
	//Singleton Design
	private static AccountDAOImpl repo = new AccountDAOImpl();
	
	private AccountDAOImpl() {}
	
	public static AccountDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the AccountDAOImpl class to exist at a time

	@Override
	public boolean insertAccount(Account account) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "INSERT INTO accounts (balance, status, account_type)" 
					+ "VALUES(?,?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(++index, account.getBalance());
			statement.setInt(++index, account.getStatus().getStatusId());
			statement.setInt(++index, account.getType().getTypeId());
						
			if(statement.execute()) {
				return true;
			}
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public boolean updateAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deposit(double amt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdraw(double amt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getBalance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Account getAccountById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Account> listAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean approveAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean denyAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AccountStatus getAccountStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountType getAccountType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getAccountOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<User> getAccountOwners() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
