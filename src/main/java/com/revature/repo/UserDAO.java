package com.revature.repo;

import java.util.Set;

import com.revature.models.User;

public interface UserDAO {

	public boolean insertUser(User user);
	public boolean updateUser(User user);
	public User getUserById(int id);
	public Set<User> getAllUsers();
	
}
