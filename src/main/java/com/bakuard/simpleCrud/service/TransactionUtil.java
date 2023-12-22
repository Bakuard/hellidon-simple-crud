package com.bakuard.simpleCrud.service;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public class TransactionUtil {

    public static interface Executable {

        public void execute();
    }

    private final TransactionTemplate transactionTemplate;

    public TransactionUtil(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void commit(Executable executable) {
        transactionTemplate.execute(status -> {
            try {
                executable.execute();
                return null;
            } catch(RuntimeException e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    public <T> T commit(Supplier<T> supplier) {
        return transactionTemplate.execute(status -> {
            try {
                return supplier.get();
            } catch(RuntimeException e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
