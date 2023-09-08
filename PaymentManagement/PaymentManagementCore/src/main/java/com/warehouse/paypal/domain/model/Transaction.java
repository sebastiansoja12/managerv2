package com.warehouse.paypal.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Transaction extends TransactionBase {

    private List<Transaction> transactions;

    public Transaction() {
    }

    public Transaction setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}
