package com.revature.service;

import java.util.Set;

import com.revature.models.User;
import com.revature.repo.UserDAO;
import com.revature.repo.UserDAOImpl;

public class RegistrationService {
	
	public final UserDAO dao = UserDAOImpl.getInstance();
	
	public boolean addUser(User user) {
		Set<User> users = dao.getAllUsers();
		for (User u : users) {
			if (u.getEmail().equals(user.getEmail()) || u.getUsername().equals(user.getUsername())) {
				return false;
			}
		}
		return dao.insertUser(user);
	}
	
	public User getUserByUsername(String username) {
		return dao.getUserByUsername(username);
	}

}
