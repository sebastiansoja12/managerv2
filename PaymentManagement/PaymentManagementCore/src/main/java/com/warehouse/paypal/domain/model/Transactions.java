package com.warehouse.paypal.domain.model;

import java.util.List;

import com.paypal.base.rest.PayPalModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Transactions extends PayPalModel {
    private List<Transactions> transactions;

    public Transactions() {
    }

    public Transactions setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
        return this;
    }

}
