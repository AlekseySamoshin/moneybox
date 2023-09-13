package com.samoshin.service;

import org.springframework.transaction.event.TransactionalApplicationListener;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class TransactionInterceptor implements CallbackHandler {

    private int beginTransactionCount = 0;
    private int commitCount = 0;
    private int rollbackCount = 0;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof TransactionalApplicationListener.SynchronizationCallback) {
                TransactionalApplicationListener.SynchronizationCallback syncCallback = (TransactionalApplicationListener.SynchronizationCallback) callback;
                if (syncCallback.isBeforeCompletion()) {
                    // Вызывается перед окончанием транзакции
                    commitCount++;
                } else {
                    // Вызывается при rollback транзакции
                    rollbackCount++;
                }
            } else if (callback instanceof TransactionStartCallback) {
                // Вызывается при начале транзакции
                beginTransactionCount++;
            }
        }
    }

    public int getBeginTransactionCount() {
        return beginTransactionCount;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public int getRollbackCount() {
        return rollbackCount;
    }
}
