package com.application.atm.view;

import java.util.Scanner;

import com.application.atm.data.models.Account;

public class CustomerView 
{
	private HDFCUserView userView;
	private Scanner sc;

	public CustomerView(HDFCUserView view)
	{
		sc=new Scanner(System.in);
		this.userView=view;
	}

	public void sessionManager(Account currentAccount)
	{
		UserMenuItems choice = null;
		int userInput=0 , userServicesCount = UserMenuItems.values().length;
		do {
			userView.printUserServices();
			this.printCustomerServices();
			userInput=Integer.parseInt(sc.nextLine());

			if(userInput <= userServicesCount)
			{
				choice = UserMenuItems.getValue(userInput);
				if(!userView.serviceController(choice))
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
