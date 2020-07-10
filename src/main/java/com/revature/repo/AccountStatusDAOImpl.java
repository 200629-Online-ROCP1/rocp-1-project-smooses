package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.AccountStatus;
import com.revature.util.ConnectionUtil;

public class AccountStatusDAOImpl implements AccountStatusDAO {

	//Singleton Design
	private static AccountStatusDAOImpl repo = new AccountStatusDAOImpl();
	
	private AccountStatusDAOImpl() {}
	
	public static AccountStatusDAOImpl getInstance() {
		return repo;
	}
	//Only allows one instance of the DAO Implementation class to exist at a time
	
	@Override
	public AccountStatus getAccountStatusByID(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM account_status WHERE status_id = ?";			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return new AccountStatus(result.getInt("status_id"), result.getString("account_status"));
			}	
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
	

}
