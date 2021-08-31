package com.application.atm.data.models;

public class Transaction 
{

	private int transactionNo;
	private float ammount;
	private float currBalanceAfterTransaction;
	private TransactionType type;
	private String source;
	
	
	public Transaction(float ammount,TransactionType type, String source) {
		this.ammount = ammount;
		this.type = type;
		this.source=source;
	}
	
	public void setCurrBalanceAfterTransaction(float currBalanceAfterTransaction) {
		this.currBalanceAfterTransaction = currBalanceAfterTransaction;
	}
	
	public float getAmmount() {
		return ammount;
	}

	public TransactionType getType() {
		return type;
	}
	public int getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(int transactionNo) {
		this.transactionNo = transactionNo;
	}

	public float getCurrBalanceAfterTransaction() {
		return currBalanceAfterTransaction;
	}
	
	 public String getSource() {
		return source;
	}

	public static class Error {
	        public static final int INSUFFICIENT_FUNDS = 1;
	        public static final int ATM_LOW_ON_CASH = 2;
	        public static final int ACCOUNT_NOT_FOUND = 3;
	        public static final int SERVER_ERROR = 4;
	    }
}
