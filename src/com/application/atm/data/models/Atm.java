package com.application.atm.data.models;


public class Atm 
{
	//TODO there is no major distinction between Atm & AtmDetails. Why two classes ?
	private final int atmId;
	private String location;
	private float availableAmount;
	private final String bankName;
	private final String atmName;
	
	public Atm(int atmId, String location, float availableAmount,String bankName) {
	
		this.atmId = atmId;
		this.location = location;
		this.availableAmount = availableAmount;
		this.bankName = bankName;
		this.atmName = bankName+"ATM";
	}
	
	public void depositeMoney(float amount)
	{
		setAvailableAmount(amount+getAvailableAmount());
	}
	public void withDrawMoney(float amount)
	{
		setAvailableAmount(getAvailableAmount()-amount);
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	public float getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(float availableAmount) {
		this.availableAmount = availableAmount;
	}
	public int getAtmId() {
		return atmId;
	}
	public String getBankName() {
		return bankName;
	}
	public String getLocation() {
		return location;
	}

	public String getAtmName() {
		return atmName;
	}
	
	

}
