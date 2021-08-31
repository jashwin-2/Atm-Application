package com.application.atm.data.models;

public enum TransactionType {
	WITHDRAW,DEPOSITE,TRANSFERRED,RECEIVED;
	public String toString()
	{
		switch(this)
		{
		case WITHDRAW:
			return "Withdraw";
		case DEPOSITE:
			return "Deposite";
		case TRANSFERRED:
			return "Transferred to";
		case RECEIVED:
			return "Received from";
		}
		return null;
	}
	public String getResult()
	{
		if(this.equals(DEPOSITE) || this.equals(RECEIVED))
			return "Credited";
		else
			return "Debited";
	}
}
