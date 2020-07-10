package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.AccountType;
import com.revature.util.ConnectionUtil;

public class AccountTypeDAOImpl implements AccountTypeDAO{
	
	//Singleton Design
	private static AccountTypeDAOImpl repo = new AccountTypeDAOImpl();
	
	private AccountTypeDAOImpl() {}
	
	public static AccountTypeDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the DAO Implementation class to exist at a time
	
	@Override
	public AccountType getAccountTypeById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM account_type WHERE type_id = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new AccountType(result.getInt("type_id"), result.getString("account_type"));
			}	
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
	
	

}
