package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import com.revature.models.User;
import com.revature.repo.UserDAOImpl;
import com.revature.util.ConnectionUtil;

public class Driver {

	public static void main(String[] args) {
		
	//Test Connection
		try(Connection conn = ConnectionUtil.getConnection()){
			System.out.println("connection successful");
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		System.out.println("==========");
	
	//Test UserDAOImpl
		UserDAOImpl testUserDAO = UserDAOImpl.getInstance();
		User testUser = testUserDAO.getUserById(3);
		System.out.println(testUser);
		System.out.println("==========");

		Set<User> allUsers = testUserDAO.getAllUsers();
		for(User u : allUsers) {
			System.out.println(u);
		}
		System.out.println("==========");
	}
}