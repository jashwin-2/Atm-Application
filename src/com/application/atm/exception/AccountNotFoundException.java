package com.application.atm.exception;

public class AccountNotFoundException extends AuthenticationFailedException {
    private final int accountId;

    public AccountNotFoundException(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }
}
