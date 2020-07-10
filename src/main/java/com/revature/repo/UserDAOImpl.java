package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.models.User;
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
			String sql = "INSERT INTO users (username, password, first_name, last_name, email, user_role)" 
					+ "VALUES(?,?,?,?,?,?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(++index, user.getUsername());
			statement.setString(++index, user.getPassword());
			statement.setString(++index, user.getFirstName());
			statement.setString(++index, user.getLastName());
			statement.setString(++index, user.getEmail());
			statement.setObject(++index, user.getRole());
			
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
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtil.getConnection()){
			
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
	

}
