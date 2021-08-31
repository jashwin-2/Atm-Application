package com.application.atm.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.application.atm.data.BankRepository;
import com.application.atm.data.RepositoryDispatcher;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.AtmDetails;
import com.application.atm.data.models.Transaction;

public class AtmServices 
{
	private RepositoryDispatcher dispatcher;


	public AtmServices(RepositoryDispatcher dispatcher , AtmDetails details) 
	{
		this.dispatcher = dispatcher;
	}

	public BankRepository getRepository(String atmNo)
	{ 
		try {
			String code="";
			int i=0;
			while( atmNo.length()>i && !Character.isDigit(atmNo.charAt(i)) )
				code=code+atmNo.charAt(i++);
			return dispatcher.getRepository(code);
		}
		catch(Exception exception)
		{
			System.out.println(exception.getLocalizedMessage());
			return null;
		}
	}

	public int getAccountNo(String atmNo)
	{
		int i=0;
		try {
		while(!Character.isDigit(atmNo.charAt(i++)));
		return Integer.parseInt(atmNo.substring(i-1));
		}
		catch(Exception exception)
		{
			return 0;
		}
		
	}
	public List<Transaction> miniStateMent(Account acc)
	{
		if(acc.getTransactions().size()<=5)
			return acc.getTransactions();
		else
		{
			int size=acc.getTransactions().size();
			return acc.getTransactions().subList(size-5,size);
		}
	}

	public Map<String, String> getAvilableBanks()
	{
		Map<String , String> banksCodes =new HashMap<>();
		for(Entry<String, BankRepository> entry : dispatcher.getBanks().entrySet())
			banksCodes.put(entry.getKey(), entry.getValue().getName());
		return banksCodes;

	}
}
