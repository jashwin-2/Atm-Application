package com.application.atm.data.models;
import java.util.ArrayList;
import java.util.List;

public class Account
{
	private final int accNo;
	private int pin;
	private User user;
	private float balance;
	private String atmNumber;
	private List<Transaction> transactions;
	private String bankName;


	public Account(int accNo, int pin,User user , float balance,String bankName,List<Transaction> transactions)
	{
		this.bankName = bankName;
		this.atmNumber=bankName+""+accNo;
		this.accNo=accNo;
		this.user = user;
		this.transactions=transactions;
		this.balance = balance;
		this.pin = pin;
		
	}

	@Override
	public String toString() {
		return "Acc No :"+accNo+"\nName : "+user.getName()+"\nBank Name :"+this.bankName+"\nMobile No :" +user.getMobileNumber()+"\nAtm Number : "+atmNumber+"\n";
	}

	public String getAtmNumber() {
		return atmNumber;
	}

	public void setAtmNumber(String atmNumber) {
		this.atmNumber = atmNumber;
	}

	public List<Transaction> getTransactions() 
	{
		return transactions;
	}

	
	public String getAccHolderName() {
		return user.getName();
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getPin() {
		return pin;
	}

	public int getAccNo() {
		return accNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public long getMobileNo() {
		return user.getMobileNumber();
	}

	public void setMobileNo(long mobileNo) {
		user.setMobileNumber(mobileNo);
	}

	public User getUser() {
		return user;
	}

	public void addTransactions(Transaction transaction) {
		if(transactions == null)
			transactions = new ArrayList<>();
		transaction.setTransactionNo(transactions.size()+1);
		transaction.setCurrBalanceAfterTransaction(balance);
		transactions.add(transaction);
	}


}
