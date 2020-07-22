package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.MoneyDTO;
import com.revature.models.MonthsDTO;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.models.UserAccountDTO;
import com.revature.service.AccountService;
import com.revature.service.UserService;

public class AccountController {

	public static final AccountService as = new AccountService();
	public static final UserService us = new UserService();
	public static final ObjectMapper om = new ObjectMapper();

	private static final Role adminRole = new Role(1, "Administrator");
	private static final Role empRole = new Role(2, "Employee");
	private static final Role vipUser = new Role(4, "Premium");

	public void handleGet(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);
			Account account = as.getAccountById(id);
			if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)
					|| as.isAccountOwner(account, currentUser)) {
				res.setStatus(200);
				String json = om.writeValueAsString(account);
				res.getWriter().println(json);
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to view that information.");
			}
		} else if (portions.length == 3) {
			switch (portions[1]) {
			case "status":
				if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)) {
					int statusId = Integer.parseInt(portions[2]);
					AccountStatus status = new AccountStatus(statusId);
					Set<Account> all = as.getAccountsByStatus(status);
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(all));
				} else {
					res.setStatus(401);
					res.getWriter().println("You do not have permission to view that information.");
				}
				break;
			case "owner":
				int id = Integer.parseInt(portions[2]);
				if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)
						|| currentUser.getUserId() == id) {
					User owner = us.getUserById(id);
					Set<Account> all = as.getAccountsByUser(owner);
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(all));
				} else {
					res.setStatus(401);
					res.getWriter().println("You do not have permission to view that information.");
				}
				break;
			}
		} else {
			if (currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)) {
				Set<Account> all = as.getAllAccounts();
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
			if (portions[1].equals("owner")) {
				UserAccountDTO dto = getUserAccountFromBody(req);
				User user = dto.getUser();
				Account account = dto.getAccount();
				if (currentUser.getRole().equals(vipUser) && as.isPrimaryOwner(account, currentUser)) {
					if (as.addNewAccountOwner(account, user)) {
						res.setStatus(201);
						res.getWriter().println("Joint account owner added: ");
						Set<User> owners = as.getAccountOwners(account);
						res.getWriter().println(om.writeValueAsString(owners));
					}
				} else {
					res.setStatus(401);
					res.getWriter().println("You do not have permission to perform that operation.");
				}
			} else {
				MoneyDTO md = getMoneyDTOFromBody(req);
				Account account = as.getAccountById(md.sourceAccountId);
				if (currentUser.getRole().equals(adminRole) || as.isAccountOwner(account, currentUser)) {
					switch (portions[1]) {
					case "withdraw":
						if (as.withdraw(md)) {
							System.out.println("Withdraw " + md);
							res.setStatus(201);
							account = as.getAccountById(md.sourceAccountId);
							res.getWriter().println("Withdraw Successful");
							res.getWriter().println(om.writeValueAsString(account));
						} else {
							res.setStatus(400);
							res.getWriter()
									.println("Withdraw failed.\nPlease check that account has sufficient funds.");
						}
						break;
					case "deposit":
						if (as.deposit(md)) {
							System.out.println("Deposit " + md);
							res.setStatus(201);
							account = as.getAccountById(md.sourceAccountId);
							res.getWriter().println("Deposit Successful");
							res.getWriter().println(om.writeValueAsString(account));
						} else {
							res.setStatus(400);
							res.getWriter().println("Deposit failed.\nPlease check account information.");
						}
						break;
					case "transfer":
						if (as.transfer(md)) {
							System.out.println("Transfer " + md);
							res.setStatus(201);
							account = as.getAccountById(md.getSourceAccountId());
							Account account2 = as.getAccountById(md.getTargetAccountId());
							res.getWriter().println("Transfer Successful");
							res.getWriter().println("Source Account :" + om.writeValueAsString(account));
							res.getWriter().println("Target Account :" + om.writeValueAsString(account2));
						} else {
							res.setStatus(400);
							res.getWriter().println(
									"Transfer failed.\nPlease check accounts are active and\nensure source account has sufficient funds.");
						}
						break;

					}
				} else {
					res.setStatus(401);
					res.getWriter().println("You do not have permission to perform that operation.");
				}
			}
		} else {
			UserAccountDTO dto = getUserAccountFromBody(req);
			User user = dto.getUser();
			Account account = dto.getAccount();
			if ((currentUser.getRole().equals(adminRole) || currentUser.getRole().equals(empRole)
					|| currentUser.equals(user)) && account.getAccountId() == 0) {
				if (as.openNewAccount(account, user)) {
					res.setStatus(201);
					res.getWriter().println("New Account Created. Pending Approval.");
					res.getWriter().println(om.writeValueAsString(account));
				} else {
					res.setStatus(400);
					res.getWriter().println("Account Creation Failed.");
				}
			} else {
				res.setStatus(401);
				res.getWriter().println("You do not have permission to perform that operation.");
			}
		}
	}

	public void handlePut(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (currentUser.getRole().equals(adminRole)) {
			if (portions.length == 2) {
				Account account = as.getAccountById(Integer.parseInt(portions[1]));
				if (req.getParameter("action") != null) {
					switch (req.getParameter("action")) {
					case "approve":
						as.approveAccount(account);
						res.setStatus(200);
						account = as.getAccountById(Integer.parseInt(portions[1]));
						res.getWriter().println("Account Approved: ");
						res.getWriter().println(om.writeValueAsString(account));
						break;
					case "deny":
						as.denyAccount(account);
						res.setStatus(200);
						account = as.getAccountById(Integer.parseInt(portions[1]));
						res.getWriter().println("Account Denied: ");
						res.getWriter().println(om.writeValueAsString(account));
						break;
					case "close":
						as.closeAccount(account);
						res.setStatus(200);
						account = as.getAccountById(Integer.parseInt(portions[1]));
						res.getWriter().println("Account Closed: ");
						res.getWriter().println(om.writeValueAsString(account));
						break;
					}
				}
			} else {
				Account account = getAccountFromBody(req);
				if (as.updateAccount(account)) {
					res.setStatus(200);
					account = as.getAccountById(account.getAccountId());
					res.getWriter().println("Account Updated");
					res.getWriter().println(om.writeValueAsString(account));
				} else {
					res.setStatus(400);
					res.getWriter().println("Account Update Failed.");
				}
			}
		} else {
			res.setStatus(401);
			res.getWriter().println("You do not have permission to perform that operation.");
		}
	}

	public void handleTime(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (currentUser.getRole().equals(adminRole)) {
			BufferedReader reader = req.getReader();
			StringBuilder s = new StringBuilder();
			String line = reader.readLine();

			while (line != null) {
				s.append(line);
				line = reader.readLine();
			}

			String body = new String(s);
			System.out.println(body);
			MonthsDTO months = om.readValue(body, MonthsDTO.class);
			System.out.println(months);
			if (as.accrueInterest(months.getNumOfMonths())) {
				res.setStatus(200);
				res.getWriter().println(months + " months of interest has been accrued for all Savings Accounts.");
			} else {
				res.setStatus(400);
				res.getWriter().println("Operation failed. No interest has been accrued.");
			}
		} else {
			res.setStatus(401);
			res.getWriter().println("You do not have permission to perform that operation.");
		}
	}

	private static MoneyDTO getMoneyDTOFromBody(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println(body);
		MoneyDTO money = om.readValue(body, MoneyDTO.class);
		System.out.println(money);
		return money;
	}

	private static UserAccountDTO getUserAccountFromBody(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println(body);
		UserAccountDTO user = om.readValue(body, UserAccountDTO.class);
		System.out.println(user);
		return user;

	}

	private static Account getAccountFromBody(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}

		String body = new String(s);
		System.out.println(body);
		Account account = om.readValue(body, Account.class);
		System.out.println(account);
		return account;
	}

	public void handleDelete(HttpServletRequest req, HttpServletResponse res, String[] portions) throws IOException {
		HttpSession ses = req.getSession(false);
		User currentUser = (User) ses.getAttribute("user");
		if (currentUser.getRole().equals(adminRole)) {
			if (portions.length == 2) {
				int id = Integer.parseInt(portions[1]);
				Account account = as.getAccountById(id);
				if (account != null) {
					if (as.deleteAccount(account)) {
						res.setStatus(200);
						res.getWriter().println("Account Deleted");
					} else {
						res.setStatus(400);
						res.getWriter().println("Account not deleted.");
					}
				} else {
					res.setStatus(404);
					res.getWriter().println("Account does not exist.");
				}
			} else {
				res.setStatus(400);
				res.getWriter().println("Please specify an account to delete.");
			}
		} else {
			res.setStatus(401);
			res.getWriter().println("You do not have permission to perform that operation.");
		}		
	}

}
