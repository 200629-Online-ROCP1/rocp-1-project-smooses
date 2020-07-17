package com.revature.controllers;

import java.util.Set;

import com.revature.models.User;
import com.revature.service.UserService;

public class UserController {
	
	private final UserService us = new UserService();
	
	public Set<User> findAll() {
		return us.getAllUsers();
	}
	
	public User findById(int id) {
		return us.getUserById(id);
	}

	public boolean addUser(User u) {
		return us.addUser(u);
		
	}
}
