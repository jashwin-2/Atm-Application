package com.application.atm.data.models;

public class AtmDetails 
{
	private int atmId;
	private String location;
	private float availableAmount;
	private String bankName;
	private String BankCode;

	public AtmDetails(int atmId, String location, float totalAmmount, String bankName)
	{
		this.atmId = atmId;
		this.location = location;
		this.setAvailableAmount(totalAmmount);
		this.bankName = bankName;
		this.BankCode= bankName;
	}

	public String getLocation() {
		return location;
	}
	public String getBankName() {
		return bankName;
	}
	public String getBankCode() {
		return BankCode;
	}
	public int getAtmId() {
		return atmId;
	}

	public float getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(float availableAmount) {
		this.availableAmount = availableAmount;
	}
}
