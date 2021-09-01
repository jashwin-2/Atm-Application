package com.application.atm.view;
import java.util.Map.Entry;

import static java.lang.System.out;
import java.util.Scanner;
import com.application.atm.data.BankRepository;
import com.application.atm.data.TransactionListener;
import com.application.atm.data.models.*;
import com.application.atm.exception.*;


public class HDFCUserView implements ATMUserView
{
	protected Atm atm;
	protected Account currentAccount;
	protected BankRepository repository;
	protected Scanner sc;
	private AtmServices services;

	public HDFCUserView(AtmServices services,Atm atm , Account account , BankRepository repository)
	{
		this.repository=repository;
		this.currentAccount=account;
		this.atm=atm;
		this.services = services;
		sc=new Scanner(System.in);
	}

	@Override
	public void onAuthenticationFailed(AuthenticationFailedException e) {
		if (e instanceof AccountNotFoundException) {
			out.println("No account found for ID " + ((AccountNotFoundException) e).getAccountId());
		} else if (e instanceof InvalidPINException) {
			out.println("Entered PIN is incorrect");
		}
		onExit();
	}
	@Override
	public void onExit() {
		repository=null;
		currentAccount=null;
	}

	@Override
	public void sessionManager()
	{
		UserMenuItems choice = null;
		do {
			printUserServices();

			try {
				int userInput=Integer.parseInt(sc.nextLine());
				choice=UserMenuItems.getValue(userInput);
			}
			catch(Exception excep)
			{
				System.out.println("Invalid input "+excep);
				choice=null;
			}

		}while(choice==null || serviceController(choice));
	}


	protected void printUserServices() {

		System.out.println("\n************* Customer Menu************\n");
		System.out.println("Name :"+currentAccount.getAccHolderName()+"  Bank :"+currentAccount.getBankName()+" Current Balance:"+currentAccount.getBalance()+" Registered Mobile no : "+currentAccount.getMobileNo());
		System.out.println("Enter your choice ");
		int ind=1;
		for(UserMenuItems opt : UserMenuItems.values())
			System.out.println(ind+++"--> "+opt);		

	}

	@Override
	public boolean serviceController(UserMenuItems choice) 
	{

		switch(choice)
		{
		case WITHDRAW:
			this.withdraw(currentAccount);
			break;


		case MONEY_TRANSFER:
			this.moneyTransfer(currentAccount);
			break;

		case MINI_STATEMENT:
			this.printMinistateMent(currentAccount);
			break;

		case EXIT:
			System.out.println("******* Thanks for Banking With Us*******\n");
			return false;
		default:
			System.out.println("Invalid Input ");
		}
		return true;

	}

	public void withdraw(Account acc)
	{
		float amount=0;
		System.out.println("Enter the ammount you want to widhdraw");
		try {
			amount=Float.parseFloat(sc.nextLine());
			if(amount<=0)
				throw new Exception();
		}
		catch(Exception exp)
		{
			System.out.println("Invalid input");
			withdraw(acc);
		}
		CashTransaction transaction = new CashTransaction(amount, TransactionType.WITHDRAW, atm );
		repository.cashTransaction(currentAccount.getAccNo(), transaction, new TransactionListener() {
			@Override
			public void onTransactionSucceeded() {
				out.println("Transaction Successful...!");
				out.println("Please collect your cash.");
				showCurrentBalance();
			}

			@Override
			public void onTransactionFailed(int errorCode) {
				switch (errorCode) {
				case Transaction.Error.ATM_LOW_ON_CASH:
					out.println("Sorry, ATM is low on cash");
					break;
				case Transaction.Error.INSUFFICIENT_FUNDS:
					out.println("Your account doesn't have enough balance");
					break;
				}
			}
		});
	}

	private void showCurrentBalance() {
		System.out.println("Current Account Balance\t:" + currentAccount.getBalance());
	}


	public void moneyTransfer(Account acc)
	{
		int receiver=0;
		float amount=0;
		String code = currentAccount.getBankName();
		System.out.println("\nEnter your choice ");
		System.out.println("1 -->  To " + currentAccount.getBankName()+" Accounts \n2 --> To Other Bank Accounts");
		try {
			if(Integer.parseInt(sc.nextLine())==2)
			{
				printAvailbleBanksCode();
				System.out.println("PRESS Q To Cancel\nEnter receivers Bank Code \\ Q (Quit)");
				code=sc.nextLine();
				if(code.equalsIgnoreCase("Q"))
					return;
			}
			System.out.println("Enter receivers Account Number ");
			receiver=Integer.parseInt(sc.nextLine());
			System.out.println("Enter the ammount you want to transfer");
			amount=Float.parseFloat(sc.nextLine());
		}
		catch(Exception exp)
		{
			System.out.println("Invalid input ");
			moneyTransfer(acc);
		}
		FundTransfer transfer;

		if(code.equals(currentAccount.getBankName())) 
		{
			if(currentAccount.getAccNo()==receiver)
			{
				System.out.println("Cant transfer to the same account");
				return;
			}
			transfer = new FundTransfer(amount, TransactionType.TRANSFERRED, currentAccount.getAccNo(),receiver , atm.getAtmName() , FundTransfer.Type.WITHIN_BANK,currentAccount.getBankName());

		}
		else {
			BankRepository receiverBank=this.services.getRepository(code);
			if(receiverBank!=null)
				transfer = new BetweenBanks(amount, TransactionType.TRANSFERRED, currentAccount.getAccNo(),receiver ,receiverBank,atm.getAtmName() ,receiverBank.getName());
			else {
				System.out.println("Invalid input can't find the bank");
				return;
			}
		}
		repository.fundTransfer(currentAccount.getAccNo(), transfer, new TransactionListener() {
			@Override
			public void onTransactionSucceeded() {
				out.println("Funds transferred successfully!");
				showCurrentBalance();
			}

			@Override
			public void onTransactionFailed(int errorCode) {
				switch (errorCode) {
				case Transaction.Error.ACCOUNT_NOT_FOUND:
					out.println("Receiver's account not found");
					break;
				case Transaction.Error.INSUFFICIENT_FUNDS:
					out.println("Your account doesn't have enough balance");
					break;
				}
			}
		});

	}

	public void printMinistateMent(Account acc)
	{
		System.out.println("************* Mini StateMent **********");
		System.out.println("Description                 "+"     "+"     Credit Or  Debit "+"          "+"CurrBalance");
		for(Transaction transaction : services.miniStateMent(acc))
		{
			if(transaction instanceof CashTransaction)
				System.out.println(transaction.getType()+" from "+transaction.getSource()+"\t"+transaction.getAmmount()+"  "+transaction.getType().getResult()+"         "+transaction.getCurrBalanceAfterTransaction());
			else if(transaction instanceof FundTransfer)
			{
				int accNo=0;
				FundTransfer transfer= (FundTransfer) transaction;
				accNo=transfer.getType()==TransactionType.TRANSFERRED ? transfer.getReceiverAccNo() : transfer.getSender();
				System.out.println(transaction.getType()+" "+transfer.getSecondPartyBank()+accNo+" from "+transaction.getSource()+"   "+transaction.getAmmount()+"  "+transaction.getType().getResult()
						+"         "+transaction.getCurrBalanceAfterTransaction());
			}
		}
		System.out.println("*****************************************\n");
	}


	private void printAvailbleBanksCode()
	{
		System.out.println("*******Available Banks and Codes **********");
		System.out.println("Bank Name           Bank Code ");
		for(Entry<String, String> entry : services.getAvilableBanks().entrySet())
			if(!entry.getKey().equals(currentAccount.getBankName()))
				System.out.println(entry.getValue()+"	--->  "+entry.getKey());
	}

}
