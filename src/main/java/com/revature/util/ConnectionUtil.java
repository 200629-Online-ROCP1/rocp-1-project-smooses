package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	
	public static Connection getConnection() throws SQLException {
		
		String url = "jdbc:postgresql://localhost:5432/bank";
		String username = "postgres";
		String password = "revature"; 
		
		return DriverManager.getConnection(url, username, password);
		
	}

}