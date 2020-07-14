package com.revature.models;

public class UserAccount {

		private Account account;
		private User user;
		private boolean primaryUser;
		public UserAccount() {
		}
		public UserAccount(Account account, User user, boolean primaryUser) {
			this.account = account;
			this.user = user;
			this.primaryUser = primaryUser;
		}
		public UserAccount(Account account, User user) {
			this.account = account;
			this.user = user;
		}
		
		public UserAccount(int accountId, int userId, boolean primaryUser) {
			
		}
		public UserAccount(int accountId, int userId) {
			
		}
		public Account getAccount() {
			return account;
		}
		public void setAccount(Account account) {
			this.account = account;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public boolean isPrimaryUser() {
			return primaryUser;
		}
		public void setPrimaryUser(boolean primaryUser) {
			this.primaryUser = primaryUser;
		}
		@Override
		public String toString() {
			return account.toString() + user.toString();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((account == null) ? 0 : account.hashCode());
			result = prime * result + (primaryUser ? 1231 : 1237);
			result = prime * result + ((user == null) ? 0 : user.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UserAccount other = (UserAccount) obj;
			if (account == null) {
				if (other.account != null)
					return false;
			} else if (!account.equals(other.account))
				return false;
			if (primaryUser != other.primaryUser)
				return false;
			if (user == null) {
				if (other.user != null)
					return false;
			} else if (!user.equals(other.user))
				return false;
			return true;
		}
		
		
}
