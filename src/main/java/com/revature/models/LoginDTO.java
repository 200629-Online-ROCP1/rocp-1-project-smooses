package com.revature.models;

import com.revature.repo.UserDAO;
import com.revature.repo.UserDAOImpl;

public class LoginDTO {

	public String username;
	public String password;
	
	UserDAO ud = UserDAOImpl.getInstance();
	User u = ud.getUserByUsername(username);
	
	public LoginDTO() {}
	
	public LoginDTO(String u, String p) {
		username = u;
		password = p;
		this.u = ud.getUserByUsername(username);
	}
	
	public boolean validate() {
		u = ud.getUserByUsername(username);
		if (u.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Role getRole() {
		u = ud.getUserByUsername(username);
		return u.getRole();
	}
}
