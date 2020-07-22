package com.revature.service;

import java.util.Set;

import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.models.MoneyDTO;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.repo.AccountDAO;
import com.revature.repo.AccountDAOImpl;
import com.revature.repo.UserAccountDAO;
import com.revature.repo.UserAccountDAOImpl;

public class AccountService {

	public final AccountDAO dao = AccountDAOImpl.getInstance();
	public final UserAccountDAO uadao = UserAccountDAOImpl.getInstance();
	
	public Set<Account> getAllAccounts() {
		return dao.listAllAccounts();
	}
	
	public Account getAccountById(int id) {
		return dao.getAccountById(id);
	}
	
	public Set<Account> getAccountsByStatus(AccountStatus status) {
		return dao.findAccountsByStatus(status);
	}
	
	public Set<Account> getAccountsByUser(User user) {
		return uadao.getAllAccounts(user);
	}
	
	public Set<User> getAccountOwners(Account account) {
		return dao.getAccountOwners(account);
	}
	
	public boolean isAccountOwner(Account account, User user) {
		boolean isOwner = false;
		Set<User> owners = dao.getAccountOwners(account);
		for (User u:owners) {
			if (u.equals(user)) {
				isOwner = true;
			}
		}
		return isOwner;
	}
	
	public boolean isPrimaryOwner(Account account, User user) {
		return uadao.isPrimaryUser(account, user);
	}
	
	public boolean addNewAccountOwner(Account account, User user) {
		UserAccount ua = new UserAccount(account, user, false);
		return uadao.insertUserAccount(ua);
	}
	
	public boolean openNewAccount(Account account, User user) {
		dao.insertAccount(account);
		int id = dao.getNewestAccountID();
		account.setAccountId(id);
		UserAccount ua = new UserAccount(account, user);
		return uadao.insertUserAccount(ua);
	}
	
	public boolean approveAccount(Account account) {
		return dao.approveAccount(account);
	}
	
	public boolean denyAccount(Account account) {
		return dao.denyAccount(account);
	}
	
	public boolean closeAccount(Account account) {
		return dao.closeAccount(account);
	}
	
	public boolean updateAccount(Account account) {
		return dao.updateAccount(account);
	}
	
	public boolean deposit(MoneyDTO md) {
		return dao.deposit(dao.getAccountById(md.sourceAccountId), md.getAmount());
	}
	
	public boolean withdraw(MoneyDTO md) {
		return dao.withdraw(dao.getAccountById(md.sourceAccountId), md.getAmount());
	}
	
	public boolean transfer(MoneyDTO md) {
		if (dao.withdraw(dao.getAccountById(md.sourceAccountId), md.getAmount())) {
			if (dao.deposit(dao.getAccountById(md.targetAccountId), md.getAmount())) {
				return true;
			} else {
				dao.deposit(dao.getAccountById(md.sourceAccountId), md.getAmount());
				return false;
			}
		}else {
			return false;
		}
	}
	
	public boolean accrueInterest(int months) {
		for (int i = 0; i < months; i++) {
			if(!dao.accrueInterest()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteAccount(Account account) {
		return dao.deleteAccount(account);
	}
}
