package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.revature.models.Role;
import com.revature.util.ConnectionUtil;

public class RoleDAOImpl implements RoleDAO {
	
	//Singleton Design
	private static RoleDAOImpl repo = new RoleDAOImpl();
	
	private RoleDAOImpl() {}
	
	public static RoleDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the DAO Implementation class to exist at a time

	@Override
	public Role getRoleByID(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM user_roles WHERE role_id = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new Role(result.getInt("role_id"), result.getString("user_role"));
			}	
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

}
