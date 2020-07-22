package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountType;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.util.ConnectionUtil;

public class UserDAOImpl implements UserDAO {
	
	//Singleton Design
	private static UserDAOImpl repo = new UserDAOImpl();
	
	private UserDAOImpl() {}
	
	public static UserDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the UserDAOImpl class to exist at a time
	
	@Override
	public boolean insertUser(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "INSERT INTO users (username, user_password, first_name, last_name, email, user_role)" 
					+ "VALUES(?,?,?,?,?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql); //,Statement.RETURN_GENERATED_KEYS
			statement.setString(++index, user.getUsername());
			statement.setString(++index, user.getPassword());
			statement.setString(++index, user.getFirstName());
			statement.setString(++index, user.getLastName());
			statement.setString(++index, user.getEmail());
			statement.setInt(++index, user.getRole().getRoleId());
			
			//ResultSet rs = statement.getGeneratedKeys();
			
			statement.execute();
			return true;			
		}
		catch (SQLException e) {
			e.getStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateUser(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "UPDATE users SET username=?, user_password=?, first_name=?, last_name=?, email=?, user_role=? WHERE user_id=?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(++index, user.getUsername());
			statement.setString(++index, user.getPassword());
			statement.setString(++index, user.getFirstName());
			statement.setString(++index, user.getLastName());
			statement.setString(++index, user.getEmail());
			statement.setInt(++index, user.getRole().getRoleId());
			statement.setInt(++index, user.getUserId());
			
			int row = statement.executeUpdate();
			System.out.println("Row(s) Updated: " + row);
			if (row > 0) {
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public User getUserById(int id) {

		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users WHERE user_id = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new User(result.getInt("user_id"), result.getString("username"),
						result.getString("user_password"), result.getString("first_name"), 
						result.getString("last_name"), result.getString("email"), result.getInt("user_role"));
			}
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public User getUserByUsername(String username) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users WHERE username = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new User(result.getInt("user_id"), result.getString("username"),
						result.getString("user_password"), result.getString("first_name"), 
						result.getString("last_name"), result.getString("email"), result.getInt("user_role"));
			}
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
	
	@Override
	public Set<User> getAllUsers() {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
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

	@Override
	public Account openNewAccount(double balance, AccountType type, User user) {
		Account account = new Account(balance, type);

		try(Connection conn = ConnectionUtil.getConnection()){
			AccountDAO	accDAO = AccountDAOImpl.getInstance();
			accDAO.insertAccount(account);
			
			String sql = "SELECT MAX(account_id) FROM accounts;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			int accountId = -1;
			while(result.next()) {
				accountId = result.getInt(1);
			}
			account.setAccountId(accountId);
			
			UserAccount ua = new UserAccount(account, user);
			UserAccountDAO uaDAO = UserAccountDAOImpl.getInstance();
			uaDAO.insertUserAccount(ua);
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return account;
	}
	
	public int getNewestUserID() {
		int userId = -1;
		try(Connection conn = ConnectionUtil.getConnection()){
					
			String sql = "SELECT MAX(user_id) FROM users;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				userId = result.getInt(1);
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return userId;
	}

	@Override
	public Set<Account> getAllAccounts(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT accounts.account_id, accounts.balance, accounts.account_type, accounts.status FROM user_accounts " + 
					"JOIN users ON user_accounts.user_id = users.user_id " + 
					"JOIN accounts ON user_accounts.account_id = accounts.account_id " + 
					"WHERE users.user_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, user.getUserId());
			
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
	public boolean deleteUser(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "DELETE FROM users WHERE user_id=?";
		
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, user.getUserId());
		
			int row = statement.executeUpdate();
			System.out.println("Row(s) Deleted: " + row);
			if (row > 0) {
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	return false;
	}

	@Override
	public boolean upgradeToPremium(User user) {
		Role premium = new Role(4, "Premium");
		user.setRole(premium);
		if (updateUser(user)) {
			return true;
		}
		return false;
	}
	

}
