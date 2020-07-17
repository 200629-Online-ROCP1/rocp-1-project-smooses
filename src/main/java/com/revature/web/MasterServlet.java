package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controllers.UserController;
import com.revature.models.User;

public class MasterServlet extends HttpServlet{
	
	public static final ObjectMapper om = new ObjectMapper();
	public static final UserController uc = new UserController();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		res.setContentType("application/json");
		res.setStatus(404); //default code set to File Not Found, will be changed later if result is successful
		
		final String URI = req.getRequestURI().replace("/rocp-project/", "");
		
		String[] portions = URI.split("/");
		
		System.out.println(Arrays.toString(portions));
		
		try {
			switch (portions[0]) {
				case "user":
					if(portions.length==2) {
						int id = Integer.parseInt(portions[1]);
						User u = uc.findById(id);
						res.setStatus(200);
						String json = om.writeValueAsString(u);
						res.getWriter().println(json);					
					}
					else {
						if(req.getMethod().equals("POST")) {
							BufferedReader reader = req.getReader();
							StringBuilder s = new StringBuilder();
							String line = reader.readLine();
							
							while(line != null) {
								s.append(line);
								line = reader.readLine();
							}
							
							String body = new String(s);
							
							System.out.println(body);
							User u = om.readValue(body, User.class);
							System.out.println(u);
							
							if(uc.addUser(u)) {
								res.setStatus(201);
								res.getWriter().println("User was created");
							}
							else {
								res.setStatus(412);
								res.getWriter().println("User not created");
							}
							
						}
						else {
							Set<User> all = uc.findAll();
							res.setStatus(200);
							res.getWriter().println(om.writeValueAsString(all));
						}
					}
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("The id you provided is not an integer.");
			res.setStatus(400);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

}
