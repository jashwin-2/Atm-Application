package com.application.atm.data.models;

public class FundTransfer extends Transaction {
	protected int sender;
	private int receiverAccNo;
	
	public FundTransfer(float ammount,TransactionType type ,int sender , int receiver ,String source) {
		super(ammount, type ,source);
		this.sender=sender;
		this.receiverAccNo=receiver;
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
}
