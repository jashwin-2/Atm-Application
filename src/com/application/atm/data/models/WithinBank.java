package com.application.atm.data.models;

public class WithinBank extends FundTransfer{


	public WithinBank(float ammount,TransactionType type, int sender, int receiverAccNo , String source) {
		super(ammount,type, sender ,receiverAccNo ,source);
		
	}
	
}
