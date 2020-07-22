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
			if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)
					|| currentUser.getUserId() == id) {
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
		if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);
			User u = us.getUserById(id);
			if (currentUser.getRole().equals(adminRole) || currentUser.getUserId() == id) {
				if (req.getParameter("action") != null) {
					System.out.println("Performing action: " + req.getParameter("action"));
					switch (req.getParameter("action")) {
					case "upgrade":
						System.out.println("Attempting to upgrade user account");
						if (us.upgradeToPremium(u)) {
							System.out.println("Account upgraded.");
							u = us.getUserById(u.getUserId());
							res.setStatus(202);
							res.getWriter().println("User " + u.getUserId() + " upgraded: \n");
							String json = om.writeValueAsString(u);
							res.getWriter().println(json);
						} else {
							System.out.println("Upgrade Failed");
							res.setStatus(400);
							res.getWriter().println(
									"Unable to upgrade user.\nPlease ensure user account contains sufficient funds.");
						}
						break;
					}
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid action.");
				}
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to modify that information.");
			}
		} else {
			User u = getUserFromBody(req);
			if (currentUser.getRole().equals(adminRole) && u.getUserId() == 0) {
				if (us.addUser(u)) {
					u = us.getUserByUsername(u.getUsername());
					res.setStatus(201);
					res.getWriter().println("User " + u.getUserId() + " registered: \n");
					String json = om.writeValueAsString(u);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter()
							.println("Unable to register user.\nCheck that username or email does not already exist.");
				}
			} else if (!u.getRole().equals(us.getUserById(u.getUserId()).getRole())
					&& !currentUser.getRole().equals(adminRole)) {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to modify user roles.");
			} else if (currentUser.getRole().equals(adminRole) || currentUser.equals(u)) {
				if (us.updateUser(req, res, u)) {
					res.setStatus(202);
					res.getWriter().println("User " + u.getUserId() + " updated: \n");
					String json = om.writeValueAsString(u);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("Unable to update user.");
				}
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to do that.");
			}
		}

	}

	public void handleDelete(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (currentUser.getRole().equals(adminRole)) {
			if (portions.length == 2) {
				int id = Integer.parseInt(portions[1]);
				User user = us.getUserById(id);
				if (user != null) {
					if (us.deleteUser(user)) {
						res.setStatus(200);
						res.getWriter().println("User Deleted");
					} else {
						res.setStatus(400);
						res.getWriter().println("User not deleted.");
					}
				} else {
					res.setStatus(404);
					res.getWriter().println("User does not exist.");
				}
			} else {
				res.setStatus(400);
				res.getWriter().println("Please specify a user to delete.");
			}
		} else {
			res.setStatus(401);
			res.getWriter().println("You do not have permission to perform that operation.");
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
