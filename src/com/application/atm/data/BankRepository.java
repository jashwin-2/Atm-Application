package com.application.atm.data;

import java.util.Map;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.BetweenBanks;
import com.application.atm.data.models.CashTransaction;
import com.application.atm.data.models.FundTransfer;
import com.application.atm.data.models.Transaction;
import com.application.atm.data.models.TransactionType;
import com.application.atm.data.models.WithinBank;
import com.application.atm.exception.AccountNotFoundException;
import com.application.atm.exception.AuthenticationFailedException;
import com.application.atm.exception.InvalidPINException;

public class BankRepository {

	protected Map<Integer, Account> accounts;
	protected String bankName;

	public Account getAccount(int accountId) {
		return accounts.get(accountId);
	}

	public Account authenticate(int accountId, int pin) throws AuthenticationFailedException {
		Account account = getAccount(accountId);
		if (account == null) {
			throw new AccountNotFoundException(accountId);
		}
		if (account.getPin() != pin) {
			throw new InvalidPINException();
		}
		return account;
	}

	public void cashTransaction(int accountId, Transaction transaction, TransactionListener listener) {
		Account account = accounts.get(accountId);
		float currentBalance = account.getBalance();
		float transactionAmount = transaction.getAmmount();
		CashTransaction cashTransaction = (CashTransaction) transaction;
		if (cashTransaction.getType() == TransactionType.DEPOSITE) 
		{
			account.setBalance(currentBalance + transactionAmount);
			cashTransaction.getAtm()
			.depositeMoney(transactionAmount);
			account.addTransactions(transaction);
			listener.onTransactionSucceeded();
		}
		else if (cashTransaction.getType() == TransactionType.WITHDRAW) 
		{
			if (currentBalance >= transactionAmount) 
			{
				if (((CashTransaction) transaction).getAtm()
						.getDetails().getAvailableAmount() >= transactionAmount)
				{
					account.setBalance(currentBalance - transactionAmount);
					cashTransaction.getAtm()
					.withDrawMoney(transactionAmount);
					account.addTransactions(transaction);
					listener.onTransactionSucceeded();
				} 
				else 
					listener.onTransactionFailed(Transaction.Error.ATM_LOW_ON_CASH);

			}
			else 
				listener.onTransactionFailed(Transaction.Error.INSUFFICIENT_FUNDS);

		}
	}

	public void fundTransfer(int accountId, Transaction transaction, TransactionListener listener) {
		Account account = getAccount(accountId);
		float currentBalance , transactionAmount;
		int receiverAccNo=0;
		if(transaction instanceof WithinBank)
		{
			WithinBank fundTransfer = (WithinBank) transaction;
			Account receiver = getAccount(fundTransfer.getReceiverAccNo());
			currentBalance = account.getBalance();
			transactionAmount = fundTransfer.getAmmount();

			if (receiver == null) {
				listener.onTransactionFailed(Transaction.Error.ACCOUNT_NOT_FOUND);
			} else if (currentBalance < transactionAmount) {
				listener.onTransactionFailed(Transaction.Error.INSUFFICIENT_FUNDS);
			} else {
				account.setBalance(currentBalance - transactionAmount);
				account.addTransactions(transaction);
				receiver.setBalance(receiver.getBalance() + transactionAmount);
				FundTransfer receiverTransaction = new FundTransfer(transactionAmount, TransactionType.RECEIVED, accountId , fundTransfer.getReceiverAccNo(),transaction.getSource());
				receiver.addTransactions(receiverTransaction);
				listener.onTransactionSucceeded();
			}
		}
		else if(transaction instanceof BetweenBanks)
		{
			BetweenBanks fundTransfer = (BetweenBanks) transaction;
			receiverAccNo = fundTransfer.getReceiverAccNo();
			currentBalance = account.getBalance();
			transactionAmount = fundTransfer.getAmmount();

			if (fundTransfer.getReceiverBank().getAccount(receiverAccNo) == null) {
				listener.onTransactionFailed(Transaction.Error.ACCOUNT_NOT_FOUND);
			} else if (currentBalance < transactionAmount) {
				listener.onTransactionFailed(Transaction.Error.INSUFFICIENT_FUNDS);
			} else {
				account.setBalance(currentBalance - transactionAmount);
				account.addTransactions(transaction);
				fundTransfer.getReceiverBank().addTransferedMoney(receiverAccNo, transactionAmount, accountId ,transaction.getSource(),listener);
			}
		}
	}
	
	void addTransferedMoney(int accNo, float amount ,int sender ,String source, TransactionListener listener) 
	{
		Account receiver = getAccount(accNo);
		receiver.setBalance(receiver.getBalance() + amount);
		receiver.addTransactions( new FundTransfer(amount , TransactionType.RECEIVED, sender , accNo,source));
		listener.onTransactionSucceeded();
	}

	public String getName() {
		return bankName;
	}
	public Map<Integer, Account> getAccounts() {
		return accounts;
	}

}

