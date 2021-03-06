package com.revature.web;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controllers.AccountController;
import com.revature.controllers.LoginController;
import com.revature.controllers.RegistrationController;
import com.revature.controllers.UserController;

public class MasterServlet extends HttpServlet {

	public static final ObjectMapper om = new ObjectMapper();
	public static final LoginController lc = new LoginController();
	public static final UserController uc = new UserController();
	public static final AccountController ac = new AccountController();
	public static final RegistrationController rc = new RegistrationController();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("application/json");
		res.setStatus(404); // default code set to File Not Found,
							// will be changed later if result is successful

		final String URI = req.getRequestURI().replace("/rocp-project/", "");
		String[] portions = URI.split("/");
		System.out.println(Arrays.toString(portions));

		try {
			HttpSession ses = req.getSession(false);
			switch (portions[0]) {
			case "users":
				if (ses != null && ((Boolean) ses.getAttribute("loggedIn"))) {
					if (req.getMethod().equals("GET")) {
						uc.handleGet(req, res, portions);	
					} else if (req.getMethod().equals("DELETE")) {
						uc.handleDelete(req, res, portions);
					} else {
						uc.handlePost(req, res, portions);
					}
				} else {
					res.setStatus(401);
					res.getWriter().println("You must be logged in to do that!");
				}
				break;
			case "register":
				if (ses == null) {
						rc.handlePost(req, res, portions);
				} else {
					res.setStatus(400);
					res.getWriter().println("You are already logged in!");
				}
				break;
			case "accounts":
				if (ses != null && ((Boolean) ses.getAttribute("loggedIn"))) {

					if (req.getMethod().equals("GET")) {
						ac.handleGet(req, res, portions);
					} else if  (req.getMethod().equals("PUT")) {
						ac.handlePut(req, res, portions);
					} else if (req.getMethod().equals("DELETE")) {
						ac.handleDelete(req, res, portions);
					} else {
						ac.handlePost(req, res, portions);
					}
				} else {
					res.setStatus(401);
					res.getWriter().println("You must be logged in to do that!");
				}
				break;
			case "passTime":
				if (ses != null && ((Boolean) ses.getAttribute("loggedIn"))) {
					ac.handleTime(req, res);
				}else {
					res.setStatus(401);
					res.getWriter().println("You must be logged in to do that!");
				}
				break;
			case "login":
				lc.login(req, res);
				break;
			case "logout":
				lc.logout(req, res);
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

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
	
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
