package com.paymybuddy.exceptions;

public class FailedToInsertException  extends Exception {

        private String operation;

        public String getOperation() {
            return operation;
        }

        public FailedToInsertException(String operation) {
            this.operation = operation;
        }

}

