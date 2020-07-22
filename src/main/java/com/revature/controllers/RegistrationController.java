package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.service.RegistrationService;

public class RegistrationController {

	private static final RegistrationService rs = new RegistrationService();
	public static final ObjectMapper om = new ObjectMapper();
	
	private static final Role standard = new Role(3, "Standard");
	
	public void handlePost(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		User u = getUserFromBody(req);
		u.setRole(standard); //only allowed to create Standard users via register
		if (rs.addUser(u)) {
			u = rs.getUserByUsername(u.getUsername());
			res.setStatus(201);
			res.getWriter().println("User " + u.getUserId() + " registered: \n");
			String json = om.writeValueAsString(u);
			res.getWriter().println(json);
		} else {
			res.setStatus(400);
			res.getWriter()
					.println("Unable to register user.\nCheck that username or email does not already exist.");
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
