package com.application.atm.data;

import java.util.HashMap;

import com.application.atm.data.models.Account;
import com.application.atm.data.models.User;

public class ICICIRepository extends BankRepository{
	private static ICICIRepository instance;
	private ICICIRepository() {
		bankName="ICICI";
    }

    public static synchronized ICICIRepository getInstance() {
        if (instance == null) {
            instance = new ICICIRepository();
            (instance).fillMockData();
        }
        return instance;
    }

    private void fillMockData() {
        Account account1 = new Account(654321, 1223, new User("Ram Pa", 26, "Olapadi, Salem",6253675), 225000,"ICICI",null);
        Account account2 = new Account(654322, 5221, new User("Jashwin S", 27, "Hosur, Krishnagiri",6235645), 125000,"ICICI",null);
        Account account3 = new Account(654323, 4093, new User("kishore S", 24, "Urapakkam, Chennai",5413235), 11520,"ICICI",null);
        accounts = new HashMap<>();
        accounts.put(account1.getAccNo(), account1);
        accounts.put(account2.getAccNo(), account2);
        accounts.put(account3.getAccNo(), account3);
    }
}
