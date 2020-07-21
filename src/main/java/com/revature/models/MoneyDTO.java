package com.revature.models;

import java.text.NumberFormat;

public class MoneyDTO {

	public int sourceAccountId;
	public int targetAccountId;
	public double amount;
	
	public MoneyDTO() {}
	
	public MoneyDTO(int act, double amt) {
		sourceAccountId = act;
		amount = amt;
	}
	
	public MoneyDTO(int src, int tgt, double amt) {
		sourceAccountId = src;
		targetAccountId = tgt;
		amount = amt;
	}

	public int getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(int sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public int getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(int targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		String output = "";
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		if (targetAccountId == 0) {
			output = "[Account: " + sourceAccountId + " Amount: " + nf.format(amount) + "]";
		} else {
			output = "[Source: " + sourceAccountId + "Target: " + targetAccountId + " Amount: " + nf.format(amount) + "]";
		}
		return output;
	}
	
	

}
