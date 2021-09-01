package com.application.atm.data;

import java.util.HashMap;

import com.application.atm.data.models.Account;
import com.application.atm.data.models.User;


public class HDFCRepository extends BankRepository{
	private static HDFCRepository instance;
	private HDFCRepository() {
		bankName="HDFC";
    }

    //TODO check other methods of creating a Singleton with Pros & Cons
    public static synchronized HDFCRepository getInstance() {
        if (instance == null) {
            instance = new HDFCRepository();
            (instance).fillMockData();
        }
        return instance;
    }

    private void fillMockData() {
        Account account1 = new Account(654321, 1223, new User("Kumaran Pa", 26, "Olapadi, Salem",6253675), 225000,"HDFC",null);
        Account account2 = new Account(654322, 5221, new User("Suren S", 27, "Hosur, Krishnagiri",6235645), 125000,"HDFC",null);
        Account account3 = new Account(654323, 4093, new User("Maran S", 24, "Urapakkam, Chennai",5413235), 11520,"HDFC",null);
        accounts = new HashMap<>();
        accounts.put(account1.getAccNo(), account1);
        accounts.put(account2.getAccNo(), account2);
        accounts.put(account3.getAccNo(), account3);
    }
}
