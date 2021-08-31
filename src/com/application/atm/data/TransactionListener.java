package com.application.atm.data;

public interface TransactionListener {
    void onTransactionSucceeded();

    void onTransactionFailed(int errorCode);
}
