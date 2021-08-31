package com.application.atm.data;
import java.util.HashMap;
import java.util.Map;



public class RepositoryDispatcher
{
	private Map<String,BankRepository> banks;


	public RepositoryDispatcher() {
		
	}

	public BankRepository getRepository(String code)
	{
		if(banks.containsKey(code))
			return banks.get(code);

		else
			return null;
	}

	public void addBank(String code,BankRepository bank)
	{
		if(this.banks==null)
			this.banks=new HashMap<>();
		banks.put(code, bank);
	}

	public Map<String, BankRepository> getBanks() {
		return banks;
	}

}
