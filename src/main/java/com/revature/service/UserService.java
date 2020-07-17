package com.revature.service;

import java.util.Set;

import com.revature.models.User;
import com.revature.repo.UserDAO;
import com.revature.repo.UserDAOImpl;

public class UserService {
	
	public final UserDAO dao = UserDAOImpl.getInstance();
	
	public Set<User> getAllUsers() {
		return dao.getAllUsers();
	}
	
	public User getUserById(int id) {
		return dao.getUserById(id);
	}

	public boolean addUser(User user) {
		Set<User> users = getAllUsers();
		for (User u:users) {
			if(u.getEmail().equals(user.getEmail()) && u.getFirstName().equals(user.getFirstName()) && u.getLastName().equals(user.getLastName())) {
				return false;
			}
		}
		return dao.insertUser(user);
		
	}
}
