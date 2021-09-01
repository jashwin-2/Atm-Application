package com.application.atm.data;

import java.util.Map;
import com.application.atm.data.models.Account;
import com.application.atm.data.models.BetweenBanks;
import com.application.atm.data.models.CashTransaction;
import com.application.atm.data.models.FundTransfer;
import com.application.atm.data.models.Transaction;
import com.application.atm.data.models.TransactionType;
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
						.getAvailableAmount() >= transactionAmount)
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

	public void fundTransfer(int accountId, FundTransfer fundTransfer, TransactionListener listener) {
	 
		Account account = getAccount(accountId);
		float currentBalance = account.getBalance();
		float transactionAmount = fundTransfer.getAmmount();
		int receiverAccNo=0;
		//TODO scope of this class is only inside else branch. So it can be declared inside the else branch
		
		//TODO currentBalance & transactionAmount assignment, error handling are common to both branches. So it can be lifted out of if-else branches. Make it a habit to avoid code repetition even at micro-level
		if (currentBalance < transactionAmount) 
			listener.onTransactionFailed(Transaction.Error.INSUFFICIENT_FUNDS);

		if(fundTransfer.getFundTransferType().equals(FundTransfer.Type.WITHIN_BANK))
		{
			Account receiver = getAccount(fundTransfer.getReceiverAccNo());

			if (receiver == null) {
				listener.onTransactionFailed(Transaction.Error.ACCOUNT_NOT_FOUND);
			} else {
				account.setBalance(currentBalance - transactionAmount);
				account.addTransactions(fundTransfer);
				receiver.setBalance(receiver.getBalance() + transactionAmount);
				FundTransfer receiverTransaction = new FundTransfer(transactionAmount, TransactionType.RECEIVED, accountId ,
						fundTransfer.getReceiverAccNo(),fundTransfer.getSource(),FundTransfer.Type.WITHIN_BANK,this.getName());
				receiver.addTransactions(receiverTransaction);
				listener.onTransactionSucceeded();
			}
		}
		
		else if(fundTransfer instanceof BetweenBanks)
		{
			BetweenBanks betweenBankTransfer=(BetweenBanks)fundTransfer;
			receiverAccNo = fundTransfer.getReceiverAccNo();

			if (betweenBankTransfer.getReceiverBank().getAccount(receiverAccNo) == null) {
				listener.onTransactionFailed(Transaction.Error.ACCOUNT_NOT_FOUND);
			} else {
				account.setBalance(currentBalance - transactionAmount);
				account.addTransactions(betweenBankTransfer);
				betweenBankTransfer.getReceiverBank().addTransferedMoney(betweenBankTransfer,listener,this.getName());
			}
		}
	}


	void addTransferedMoney(BetweenBanks transfer, TransactionListener listener ,String bankName) 
	{
		Account receiver = getAccount(transfer.getReceiverAccNo());
		receiver.setBalance(receiver.getBalance() + transfer.getAmmount());
		receiver.addTransactions( new FundTransfer(transfer.getAmmount() , TransactionType.RECEIVED, transfer.getSender() , transfer.getReceiverAccNo(),transfer.getSource()
				, transfer.getFundTransferType(),bankName));
		listener.onTransactionSucceeded();
	}

	public String getName() {
		return bankName;
	}
	public Map<Integer, Account> getAccounts() {
		return accounts;
	}

}

