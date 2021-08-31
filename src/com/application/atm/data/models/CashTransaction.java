package com.application.atm.data.models;

public class CashTransaction extends Transaction{
	private Atm atm;
	
	public CashTransaction(float ammount ,TransactionType type , Atm atm) {
		super(ammount,type,atm.getName());
		this.atm=atm;
	}

	public Atm getAtm() {
		return atm;
	}

	public void setAtm(Atm atm) {
		this.atm = atm;
	}
}
