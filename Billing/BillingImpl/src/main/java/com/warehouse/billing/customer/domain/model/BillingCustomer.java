package com.warehouse.billing.customer.domain.model;

import com.warehouse.billing.customer.domain.enumeration.BillingCustomerStatus;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public class BillingCustomer {

    private final BillingCustomerId id;

    private final OperatorId operatorId;

    private final TaxId taxId;

    private final UserId userId;

    private final Instant createdAt;

    private BillingCustomerDetails details;

    private BillingCustomerStatus status;

    private Instant updatedAt;

    public BillingCustomer(final BillingCustomerId id, final OperatorId operatorId, final TaxId taxId,
                           final UserId userId, final BillingCustomerDetails details,
                           final BillingCustomerStatus status, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.operatorId = operatorId;
        this.taxId = taxId;
        this.userId = userId;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(final BillingCustomerDetails details, final Instant now) {
        this.details = details;
        this.updatedAt = now;
    }

    public void block(final Instant now) {
        if (BillingCustomerStatus.BLOCKED.equals(this.status)) {
            return;
        }
        this.status = BillingCustomerStatus.BLOCKED;
        this.updatedAt = now;
    }

    public void unblock(final Instant now) {
        if (BillingCustomerStatus.ACTIVE.equals(this.status)) {
            return;
        }
        this.status = BillingCustomerStatus.ACTIVE;
        this.updatedAt = now;
    }

    public void ensureActive() {
        if (!BillingCustomerStatus.ACTIVE.equals(this.status)) {
            throw new BillingCustomerException(BillingCustomerException.Reason.NOT_ACTIVE,
                    "Business customer is not active");
        }
    }

    public BillingCustomerId id() {
        return id;
    }

    public OperatorId operatorId() {
        return operatorId;
    }

    public TaxId taxId() {
        return taxId;
    }

    public UserId userId() {
        return userId;
    }

    public BillingCustomerDetails details() {
        return details;
    }

    public BillingCustomerStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
