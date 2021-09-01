package com.application.atm.data.models;

public class FundTransfer extends Transaction {
	String secondPartyBank;
	Type fundTransferType;
	protected int sender;
	private int receiverAccNo;
	
	public FundTransfer(float ammount,TransactionType type ,int sender , int receiver,String source , Type fundTransferType, String bankName) {
		super(ammount, type ,source);
		this.sender=sender;
		this.receiverAccNo=receiver;
		this.secondPartyBank=bankName;
		this.fundTransferType=fundTransferType;
	}
	
	public int getSender() {
		return sender;
	}

	public int getReceiverAccNo() {
		return receiverAccNo;
	}

	public void setReceiverAccNo(int receiverAccNo) {
		this.receiverAccNo = receiverAccNo;
	}
	
	public String getSecondPartyBank() {
		return secondPartyBank;
	}

	public Type getFundTransferType() {
		return fundTransferType;
	}


	public enum Type{
		WITHIN_BANK,BETWEEN_BANK;
	}
}
