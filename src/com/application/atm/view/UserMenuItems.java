package com.application.atm.view;


public enum UserMenuItems {
	WITHDRAW(1),MONEY_TRANSFER(2),MINI_STATEMENT(3),EXIT(4);

	public final int num;
	UserMenuItems(int num) {
		this.num=num;
	}

	public String toString()
	{
		switch(this)
		{
		case WITHDRAW:
			return "WithDraw";
		case MONEY_TRANSFER:
			return "Money Transfer";
		case MINI_STATEMENT:
			return "Mini statement";
		case EXIT:
			return "Exit";
		}
		return null;
	}
	
    public static UserMenuItems getValue(int num)
    {
    	for(UserMenuItems option : values())
    		if(option.num==num)
    			return option;
		return null;
    }
}
