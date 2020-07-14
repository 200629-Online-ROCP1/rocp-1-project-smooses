package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.repo.UserAccountDAO;
import com.revature.repo.UserAccountDAOImpl;
import com.revature.repo.UserDAO;
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
		UserDAO testUserDAO = UserDAOImpl.getInstance();
		User testUser = testUserDAO.getUserById(3);
		System.out.println(testUser);
		System.out.println("==========");

		printAllUsers();
		
		User user = new User("importTest","passwordPASSWORD","Import","Testuser","importtest@user.com",3);
		testUserDAO.insertUser(user);
		user.setUserId(testUserDAO.getNewestUserID());
		System.out.println("New User ID: " + user.getUserId());
		printAllUsers();
		
		user.setUsername("updateTest");
		testUserDAO.updateUser(user);
		user = testUserDAO.getUserById(user.getUserId());
		System.out.println(user);
		System.out.println("==========");
		
		testUserDAO.deleteUser(user);
		printAllUsers();
		
	//Test UserAccountDAOImpl
		UserAccountDAO testUADAO = UserAccountDAOImpl.getInstance();
		Set<Account> allAccounts = testUADAO.getAllAccounts(testUser);
		for(Account a : allAccounts) {
			System.out.println(a);
		}
		System.out.println("==========");
	}
	
	private static void printAllUsers() {
		UserDAO testUserDAO = UserDAOImpl.getInstance();
		
		Set<User> allUsers = testUserDAO.getAllUsers();
		for(User u : allUsers) {
			System.out.println(u);
		}
		System.out.println("==========");
	}
}