package com.application.atm.view;

public enum UserMenuItems {
	WITHDRAW,MONEY_TRANSFER,MINI_STATEMENT,EXIT;

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
}
