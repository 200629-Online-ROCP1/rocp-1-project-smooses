package com.revature.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public User getUserByUsername(String username) {
		return dao.getUserByUsername(username);
	}

	public boolean addUser(User user) {
		Set<User> users = getAllUsers();
		for (User u:users) {
			if(u.getEmail().equals(user.getEmail()) || u.getUsername().equals(user.getUsername())) {
				return false;
			}
		}
		return dao.insertUser(user);
	}
	
	public boolean updateUser(HttpServletRequest req, HttpServletResponse res, User user) {
		return dao.updateUser(user);
	}

	public boolean deleteUser(User user) {
		return dao.deleteUser(user);
	}
}
