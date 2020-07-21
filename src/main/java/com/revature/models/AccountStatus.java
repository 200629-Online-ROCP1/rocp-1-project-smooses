package com.revature.models;

import com.revature.repo.AccountStatusDAO;
import com.revature.repo.AccountStatusDAOImpl;

public class AccountStatus {
	  private int statusId; // primary key
	  private String status; // not null, unique

	public AccountStatus() {
	}
	
	public AccountStatus(int statusId, String status) {
		this.statusId = statusId;
		this.status = status;
	}
	public AccountStatus(String status) {
		this.status = status;
		AccountStatusDAO dao = AccountStatusDAOImpl.getInstance();
		statusId = dao.getAccountStatusID(status);
	}
	public AccountStatus(int statusId) {
		this.statusId = statusId;
		AccountStatusDAO dao = AccountStatusDAOImpl.getInstance();
		this.status = dao.getAccountStatusByID(statusId).getStatus();
	}

	public int getStatusId() {
		return statusId;
	}
	
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "[Account Status: " + status + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + statusId;
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
		AccountStatus other = (AccountStatus) obj;
		if (statusId != other.statusId)
			return false;
		return true;
	}


}