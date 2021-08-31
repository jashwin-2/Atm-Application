package com.application.atm.data.models;

import com.application.atm.data.BankRepository;

public class BetweenBanks extends FundTransfer{
		private BankRepository receiverBank;
		
		public BetweenBanks(float ammount,TransactionType type, int sender,int receiver, BankRepository receiverBank,String source) {
			super(ammount, type, sender , receiver,source);
			this.receiverBank=receiverBank;
		}

		public BankRepository getReceiverBank() {
			return receiverBank;
		}
	
	

}
