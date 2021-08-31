package com.application.atm.view;
import java.util.Map.Entry;

import static java.lang.System.out;

import java.util.Scanner;

import com.application.atm.data.BankRepository;
import com.application.atm.data.RepositoryDispatcher;
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

	public HDFCUserView(AtmServices services,Atm atm) 
	{
		this.atm=atm;
		this.services = services;
		sc=new Scanner(System.in);
	}
	@Override
	public void loginMenu()
	{
		String atmNumber;
		Account account =null;
		Scanner sc=new Scanner(System.in);
		System.out.println("*****WELCOME TO "+atm.getDetails().getBankName()+" ATM "+atm.getDetails().getLocation()+"*********\n");
		System.out.println("*********Login Menu**********\n");
		System.out.println("Enter your Atm number Number ");	
		atmNumber=sc.nextLine();
		if((repository=services.getRepository(atmNumber)) ==null && services.getAccountNo(atmNumber)!=0) 
		{
			System.out.println("Invalid ATM card No");
			onExit();
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
				onAuthenticationFailed(e);
			}
			if (currentAccount != null) {
				if(!currentAccount.getBankName().equals(atm.getDetails().getBankName()))
					this.sessionManager();
				else
					new CustomerView(this).sessionManager(currentAccount);
				onExit();

			}
		}
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
		loginMenu();
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
			catch(ArrayIndexOutOfBoundsException excep)
			{
				System.out.println("Invalid input ");
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
		System.out.println("Enter the ammount you want to widhdraw");
		float amount=Float.parseFloat(sc.nextLine());

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
		int receiver;
		String code = currentAccount.getBankName();
		System.out.println("\nEnter your choice ");
		System.out.println("1 -->  To " + currentAccount.getBankName()+" Accounts \n2 --> To Other Bank Accounts");
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
		float amount=Float.parseFloat(sc.nextLine());
		FundTransfer transfer;

		if(code.equals(currentAccount.getBankName())) 
		{
			if(currentAccount.getAccNo()==receiver)
			{
				System.out.println("Cant transfer to the same account");
				return;
			}
			transfer = new WithinBank(amount, TransactionType.TRANSFERRED, currentAccount.getAccNo(),receiver , atm.getName());

		}
		else {
			BankRepository receiverBank=this.services.getRepository(code);
			if(receiverBank!=null)
				transfer = new BetweenBanks(amount, TransactionType.TRANSFERRED, currentAccount.getAccNo(),receiver ,receiverBank,atm.getName());
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
				System.out.println(transaction.getType()+" "+accNo+" from "+transaction.getSource()+"   "+transaction.getAmmount()+"  "+transaction.getType().getResult()
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
