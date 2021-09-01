package com.application.atm.view;

import static java.lang.System.out;
import java.util.Scanner;
import com.application.atm.data.BankRepository;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.Atm;
import com.application.atm.exception.AuthenticationFailedException;

public class HDFCATMLoginView {
	
	private BankRepository repository;
	private AtmServices services;
	Account currentAccount;
	private Atm atm;

	
	public HDFCATMLoginView(BankRepository repository, AtmServices services, Atm atm) {
		this.repository = repository;
		this.services = services;
		this.atm = atm;
	}

	public void  start()
	{
		while(true)
			loginMenu();
	}
	
	public void loginMenu()
	{
		String atmNumber;
		Account account =null;
		Scanner sc=new Scanner(System.in);
		System.out.println("*****WELCOME TO "+atm.getBankName()+" ATM "+atm.getLocation()+"*********\n");
		System.out.println("*********Login Menu**********\n");
		System.out.println("Enter your Atm number Number ");	
		atmNumber=sc.nextLine();
		if((repository=services.getRepository(atmNumber)) ==null && services.getAccountNo(atmNumber)!=0) 
		{
			System.out.println("Invalid ATM card No");
		}
		int accountId=services.getAccountNo(atmNumber);
		account = repository.getAccount(accountId);

		if (account == null) {
			out.println("Invalid ATM card");
			loginMenu();
		}
		else
		{
			out.println("Please enter your pin");
			int pin = Integer.parseInt(sc.nextLine());
			try {
				currentAccount = repository.authenticate(accountId, pin);
			} catch (AuthenticationFailedException e) {
				new HDFCUserView(services,atm,account,repository).onAuthenticationFailed(e);
			}
			if (currentAccount != null) {
				if(!currentAccount.getBankName().equals(atm.getBankName()))
					new HDFCUserView(services,atm,account,repository).sessionManager();
				else
					new HDFCCustomerView(services,atm,account,repository).sessionManager(currentAccount);

			}
		}
	}
}
