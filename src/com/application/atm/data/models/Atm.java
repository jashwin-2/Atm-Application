package com.application.atm.data.models;

import com.application.atm.view.AtmServices;

public class Atm 
{
	private AtmDetails details;
	private String name;

	public Atm(AtmDetails details, AtmServices services)
	{
		setName(details.getBankName()+" ATM");
		this.details=details;
	}

	public void depositeMoney(float amount)
	{
		details.setAvailableAmount(amount+details.getAvailableAmount());
	}
	public void withDrawMoney(float amount)
	{
		details.setAvailableAmount(details.getAvailableAmount()-amount);
	}
	
	public AtmDetails getDetails() {
		return details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
