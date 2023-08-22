package com.samoshin.moneybox.exception;

public class TransactionFailException extends RuntimeException {
    public TransactionFailException(String message) {
        super(message);
    }
}
