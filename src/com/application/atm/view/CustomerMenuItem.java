package com.application.atm.view;

public enum CustomerMenuItem 
{
	CHANGE_MOBILE_NO;

	public String toString()
	{
		if(this.equals(CHANGE_MOBILE_NO))
			return "Change Mobile Number";
		return null;
	}

}
