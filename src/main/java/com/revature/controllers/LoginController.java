package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.LoginDTO;
import com.revature.service.LoginService;

public class LoginController {

	private static final LoginService ls = new LoginService();
	private static final ObjectMapper om = new ObjectMapper();
	
	public void login(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
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
			
			LoginDTO l = om.readValue(body,  LoginDTO.class);
			
			if (ls.login(l)) {
				HttpSession ses = req.getSession();
				ses.setAttribute("user", l);
				ses.setAttribute("loggedIn", true);
				res.setStatus(200);
				res.getWriter().println("Login Successful!");
			}
			else {
				HttpSession ses = req.getSession(false);
				if(ses!=null) {
					ses.invalidate();
				}
				res.setStatus(401);
				res.getWriter().println("Login Failed");
			}
		} 
	}
}

