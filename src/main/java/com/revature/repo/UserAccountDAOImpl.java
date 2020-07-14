package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.util.ConnectionUtil;

public class UserAccountDAOImpl implements UserAccountDAO{
	
	//Singleton Design
	private static UserAccountDAOImpl repo = new UserAccountDAOImpl();
	
	private UserAccountDAOImpl() {}
	
	public static UserAccountDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the UserAccountDAOImpl class to exist at a time
	

	@Override
	public boolean insertUserAccount(UserAccount ua) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "INSERT INTO user_accounts (account_id, user_id, primary_user)" 
					+ "VALUES(?,?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(++index, ua.getAccount().getAccountId());
			statement.setInt(++index, ua.getUser().getUserId());
			statement.setBoolean(++index, ua.isPrimaryUser());
						
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
	public boolean isPrimaryUser(Account account, User user) {
		boolean primary = false;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT primary_user FROM user_accounts WHERE account_id = ? AND user_id = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, account.getAccountId());
			statement.setInt(2, user.getUserId());
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				primary = result.getBoolean("primary_user");
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return primary;
	}

	@Override
	public Set<Account> getAllAccounts(User user) {
		 
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT accounts.account_id, accounts.balance, accounts.account_type, accounts.status FROM user_accounts " + 
					"JOIN users ON user_accounts.user_id = users.user_id " + 
					"JOIN accounts ON user_accounts.account_id = accounts.account_id " + 
					"WHERE users.user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1,  user.getUserId());
			
			Set<Account> set = new HashSet<Account>();
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
	public Set<User> getAllAccountOwners(Account account) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT users.user_id, users.username, users.user_password, users.first_name, users.last_name, users.email, users.user_role " + 
					"FROM user_accounts " + 
					"JOIN users ON user_accounts.user_id = users.user_id " + 
					"JOIN accounts ON user_accounts.account_id = accounts.account_id " + 
					"WHERE accounts.account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1,  account.getAccountId());
			
			Set<User> set = new HashSet<User>();
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
