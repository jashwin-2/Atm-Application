package com.application.atm.data.models;
//TODO WithinBank overrides nothing. purpose of this classs ?
public class WithinBank extends FundTransfer{


	public WithinBank(float ammount,TransactionType type, int sender, int receiverAccNo , String source) {
		super(ammount,type, sender ,receiverAccNo ,source);
		
	}
	
}
