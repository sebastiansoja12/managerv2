package com.warehouse.paypal;

import java.util.List;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Transactions;

public class PaypalFactory {

    public static List<Transactions> transactionList() {
        final Transactions transaction = new Transactions();
        transaction.setAmount(createAmount());
        return List.of(transaction);
    }

    private static Amount createAmount() {
        final Amount amount = new Amount();
        amount.setDetails(createDetails());
        return amount;
    }

    private static Details createDetails() {
        final Details details = new Details();
        details.setSubtotal("30");
        return details;
    }
}
