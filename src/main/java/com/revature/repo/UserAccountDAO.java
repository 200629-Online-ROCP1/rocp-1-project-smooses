package com.revature.repo;

import java.util.Set;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.models.UserAccount;

public interface UserAccountDAO {
	
	public boolean insertUserAccount(UserAccount ua);
	public boolean isPrimaryUser(Account account, User user);
	public Set<Account> getAllAccounts(User user);
	public Set<User> getAllAccountOwners(Account account);

}
