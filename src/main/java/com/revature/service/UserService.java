package com.revature.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.models.Account;
import com.revature.models.MoneyDTO;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.repo.UserDAO;
import com.revature.repo.UserDAOImpl;

public class UserService {

	public final UserDAO dao = UserDAOImpl.getInstance();
	public final AccountService as = new AccountService();

	public Set<User> getAllUsers() {
		return dao.getAllUsers();
	}

	public User getUserById(int id) {
		return dao.getUserById(id);
	}

	public User getUserByUsername(String username) {
		return dao.getUserByUsername(username);
	}

	public boolean addUser(User user) {
		Set<User> users = getAllUsers();
		for (User u : users) {
			if (u.getEmail().equals(user.getEmail()) || u.getUsername().equals(user.getUsername())) {
				return false;
			}
		}
		return dao.insertUser(user);
	}

	public boolean updateUser(HttpServletRequest req, HttpServletResponse res, User user) {
		return dao.updateUser(user);
	}

	public boolean deleteUser(User user) {
		return dao.deleteUser(user);
	}

	public boolean upgradeToPremium(User user) {
		Role standard = new Role(3, "Standard");
		if (!user.getRole().equals(standard)) {
			System.out.println("Unable to upgrade non-standard user account.");
			return false;
		} else {
			Set<Account> accounts = as.getAccountsByUser(user);
			for (Account a : accounts) {
				if (a.isActive() && (a.getBalance() >= 25.0)) {
					MoneyDTO premiumUpgrade = new MoneyDTO(a.getAccountId(), 25.0);
					if (as.withdraw(premiumUpgrade)) {
						if (dao.upgradeToPremium(user)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean upgradeToPremium(UserAccount ua) {
		User u = ua.getUser();
		Account a = ua.getAccount();
		Role standard = new Role(3, "Standard");
		if (!u.getRole().equals(standard)) {
			System.out.println("Unable to upgrade non-standard user account.");
			return false;
		} else if (a.isActive() && a.getBalance() >= 25.0) {
			MoneyDTO premiumUpgrade = new MoneyDTO(a.getAccountId(), 25.0);
			if (as.withdraw(premiumUpgrade)) {
				if (dao.upgradeToPremium(u)) {
					return true;
				}
			}
		} else {
			System.out.println("Account not active or insufficient funds.");
			return false;
		}
		return false;
	}
}
