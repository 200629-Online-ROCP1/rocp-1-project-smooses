package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.service.UserService;

public class UserController {

	private static final UserService us = new UserService();
	public static final ObjectMapper om = new ObjectMapper();

	private static final Role adminRole = new Role(1, "Administrator");
	private static final Role empRole = new Role(2, "Employee");

	public void handleGet(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);
			if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole) || currentUser.getUserId() == id) {
				User user = us.getUserById(id);
				res.setStatus(200);
				String json = om.writeValueAsString(user);
				res.getWriter().println(json);
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to view that information.");
			}
		} else {
			if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)) {
				Set<User> all = us.getAllUsers();
				res.setStatus(200);
				res.getWriter().println(om.writeValueAsString(all));
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to view that information.");
			}
		}
	}

	public void handlePost(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		User u = getUserFromBody(req);
		if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);
			if (!u.getRole().equals(us.getUserById(id).getRole()) && !currentUser.getRole().equals(adminRole)) {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to modify user roles.");
			}else if (currentUser.getRole().equals(adminRole) || currentUser.getUserId() == id) {
				if (us.updateUser(req, res, u)) {
					res.setStatus(202);
					res.getWriter().println("User " + u.getUserId() + " updated: \n");
					String json = om.writeValueAsString(u);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("User not updated, please double check correct information.");
				}
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to modify that information.");
			}
		} else {
			if (currentUser.getRole().equals(adminRole)) {
				if (us.addUser(u)) {
					u = us.getUserByUsername(u.getUsername());
					res.setStatus(201);
					res.getWriter().println("User " + u.getUserId() + " registered: \n");
					String json = om.writeValueAsString(u);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("Unable to add new user.\n" 
							+ "Check that username or email do not already exist.");
				}
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to register a new user.");
			}
		}
	}
	
	private static User getUserFromBody(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println(body);
		User user = om.readValue(body, User.class);
		System.out.println(user);
		return user;

	}

}
