package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
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
		try(Connection conn = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "UPDATE accounts SET balance = ?, status = ?, account_type=? WHERE account_id=?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(++index, account.getBalance());
			statement.setInt(++index, account.getStatus().getStatusId());
			statement.setInt(++index, account.getType().getTypeId());
			statement.setInt(++index, account.getAccountId());
			
			int row = statement.executeUpdate();
			System.out.println("Row(s) Updated: " + row);
			if (row > 0) {
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}		return false;
	}

	@Override
	public boolean deposit(Account account, double amt) {
		try(Connection conn = ConnectionUtil.getConnection()){
			if (amt < 0) {
				System.out.println("Can't deposit a negative amount. Try withdrawing.");
				return false;
			}
			account.setBalance(account.getBalance() + amt); 
			updateAccount(account);
			return true;		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean withdraw(Account account, double amt) {
		try(Connection conn = ConnectionUtil.getConnection()){
			if (amt < 0) {
				System.out.println("Can't withdraw a negative amount. Try depositing.");
				return false;
			}
			account.setBalance(account.getBalance() - amt); 
			updateAccount(account);
			return true;		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Account getAccountById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accounts WHERE account_id = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new Account(result.getInt("account_id"), result.getDouble("balance"),
						result.getInt("status"), result.getInt("account_type"));
			}
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Set<Account> listAllAccounts() {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accounts;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			Set<Account> set = new HashSet<>();
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				set.add(new Account(result.getInt("account_id"), result.getDouble("balance"),
						result.getInt("status"), result.getInt("account_type")));
			}
			
			return set;
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public boolean approveAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean denyAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AccountStatus getAccountStatus(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountType getAccountType(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getAccountOwner(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<User> getAccountOwners(Account account) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT users.user_id, users.username, users.user_password, users.first_name, users.last_name, users.email, users.user_role " + 
					"FROM user_accounts " + 
					"JOIN users ON user_accounts.user_id = users.user_id " + 
					"JOIN accounts ON user_accounts.account_id = accounts.account_id " + 
					"WHERE accounts.account_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, account.getAccountId());
			
			Set<User> set = new HashSet<>();
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				set.add(new User(result.getInt("user_id"), result.getString("username"),
						result.getString("user_password"), result.getString("first_name"), 
						result.getString("last_name"), result.getString("email"), result.getInt("user_role")));
			}
			
			return set;
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
	

}
