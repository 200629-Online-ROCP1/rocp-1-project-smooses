package com.revature.models;

public class UserAccountDTO {

	public User user;
	public Account account;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Override
	public String toString() {
		return "UserAccountDTO [user=" + user.getUserId() + ", account=" + account.getAccountId() + "]";
	}
	
	
}
