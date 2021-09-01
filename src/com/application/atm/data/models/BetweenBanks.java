package com.application.atm.data.models;

import com.application.atm.data.BankRepository;

public class BetweenBanks extends FundTransfer{
	private BankRepository receiverBank;


	public BetweenBanks(float ammount,TransactionType type, int sender,int receiver
			, BankRepository receiverBankRepo,String source ,String secondPartyBank) {
		
		super(ammount, type, sender , receiver,source,FundTransfer.Type.BETWEEN_BANK,secondPartyBank);
		this.receiverBank=receiverBankRepo;
	}

	public BankRepository getReceiverBank() {
		return receiverBank;
	}



}
