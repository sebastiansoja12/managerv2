package com.warehouse.billing.payment.domain.vo;

import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

public record PaymentQuoteReference(OperatorId operatorId, boolean paymentRequired, PriceQuoteId priceQuoteId,
                                    BillingCustomerId billingCustomerId) {

    public boolean createdBy(final BillingCustomerId customerId) {
        return this.billingCustomerId != null && this.billingCustomerId.equals(customerId);
    }

    public PriceQuoteId payableQuoteId() {
        if (!this.paymentRequired) {
            throw new PaymentException(PaymentException.Reason.NOT_FOUND, "Shipment does not require payment");
        }
        if (this.priceQuoteId == null) {
            throw new PaymentNotReadyException("Price quote of the shipment is not available yet");
        }
        return this.priceQuoteId;
    }
}
