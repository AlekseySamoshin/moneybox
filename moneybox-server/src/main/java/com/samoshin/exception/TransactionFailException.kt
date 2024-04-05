package com.samoshin.exception;

public class TransactionFailException extends RuntimeException {
    public TransactionFailException(String message) {
        super(message);
    }
}
