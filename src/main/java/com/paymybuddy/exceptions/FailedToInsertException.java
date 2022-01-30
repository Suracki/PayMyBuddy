package com.paymybuddy.exceptions;

public class FailedToInsertException  extends Exception {

        String operation;

        public FailedToInsertException(String operation) {
            this.operation = operation;
        }

}

