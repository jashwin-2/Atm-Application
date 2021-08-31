package com.application.atm.view;

public enum CustomerMenuItem 
{
	CHANGE_MOBILE_NO(1);

	public final int num;
	CustomerMenuItem(int num) {
		this.num=num;
	}

	public String toString()
	{
		if(this.equals(CHANGE_MOBILE_NO))
			return "Change Mobile Number";
		return null;
	}
	
	public static CustomerMenuItem getValue(int num)
	{
		for(CustomerMenuItem option : values())
    		if(option.num==num)
    			return option;
		return null;
	}

}
