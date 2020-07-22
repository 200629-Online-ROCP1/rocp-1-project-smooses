package com.revature.models;

public class MonthsDTO {

	public int numOfMonths;

	public MonthsDTO() {}

	public MonthsDTO(int numOfMonths) {
		this.numOfMonths = numOfMonths;
	}

	public int getNumOfMonths() {
		return numOfMonths;
	}

	public void setNumOfMonths(int numOfMonths) {
		this.numOfMonths = numOfMonths;
	}

	@Override
	public String toString() {
		return "" + numOfMonths;
	}
	
	
	
	
	
}
