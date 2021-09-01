package com.application.atm.view;



import com.application.atm.data.BankRepository;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.Atm;

//TODO The hierarchy should be ATMUserView > HDFCUserView > CustomerView. Here CustomerView has-a HDFCUserView instead it should be CustomerView is-a HDFCUserView and the more apt terminology is HDFCATMUserView > HDFCCustomerView
public class HDFCCustomerView extends HDFCUserView
{
	
	public HDFCCustomerView(AtmServices services,Atm atm,Account account,BankRepository repository)
	{
		super(services,atm,account,repository);
	}

	public void sessionManager(Account currentAccount)
	{
		UserMenuItems choice = null;
		int userInput=0 , userServicesCount = UserMenuItems.values().length;
		do {
			super.printUserServices();
			this.printCustomerServices();
			userInput=Integer.parseInt(sc.nextLine());

			if(userInput <= userServicesCount)
			{
				choice = UserMenuItems.getValue(userInput);
				if(!super.serviceController(choice))
					return;
			}

			else if(userInput <= userServicesCount + CustomerMenuItem.values().length)
				serviceController(CustomerMenuItem.getValue(userInput-userServicesCount),currentAccount);
			else
				System.out.println("Invalid input ");

		}while(true);
	}

	private void serviceController(CustomerMenuItem choice , Account currentAccount) 
	{
		if(choice.equals(CustomerMenuItem.CHANGE_MOBILE_NO))	
		{
			System.out.println("Enter your new mobile number");
			currentAccount.setMobileNo(Long.parseLong(sc.nextLine()));
		}
	}

	private void printCustomerServices() {
		int ind = UserMenuItems.values().length+1;
		for(CustomerMenuItem service : CustomerMenuItem.values())
			System.out.println(ind+"--> "+service);
	}
}
