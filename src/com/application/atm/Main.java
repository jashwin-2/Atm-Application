package com.application.atm;

import com.application.atm.data.RepositoryDispatcher;

import java.util.Map;
import java.util.Map.Entry;

import com.application.atm.data.BankRepository;
import com.application.atm.data.HDFCRepository;
import com.application.atm.data.ICICIRepository;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.Atm;
import com.application.atm.data.models.AtmDetails;
import com.application.atm.view.AtmServices;
import com.application.atm.view.HDFCUserView;

public class Main {

	public static void main(String[] args)
	{
		BankRepository hdfc= HDFCRepository.getInstance();
		BankRepository icici= ICICIRepository.getInstance();

		RepositoryDispatcher dispatcher=new RepositoryDispatcher();
		dispatcher.addBank("HDFC", hdfc);
		dispatcher.addBank("ICICI", icici);
		printAccountDetails(hdfc);
		printAccountDetails(icici);
		
		AtmDetails hdfcDetails=new AtmDetails(123, "chennai", 25000, "HDFC");
		Atm hdfcAtm = new Atm(hdfcDetails, new AtmServices(dispatcher,hdfcDetails));
		new HDFCUserView(new AtmServices(dispatcher, hdfcDetails), hdfcAtm).loginMenu();
	}

	private static void printAccountDetails(BankRepository bank) {
		System.out.println(bank.getName()+" Bank Accounts ");
		for(Entry<Integer, Account> entry : bank.getAccounts().entrySet())
			System.out.println("Acc No : "+entry.getKey()+"\tName : "+entry.getValue().getAccHolderName()
					+"\tATM No: "+entry.getValue().getAtmNumber());
		System.out.println();
		
	}

}
