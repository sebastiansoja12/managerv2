package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.InsuranceType;
import com.warehouse.commonassets.model.Money;

public final class InsuranceAmount {

    private final InsuranceType insuranceType;

    private final Money amount;

    public InsuranceAmount(final InsuranceType insuranceType, final Money amount) {
        this.insuranceType = insuranceType;
        this.amount = amount;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public Money getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "InsuranceType: " + insuranceType + ", Amount: " + amount;
    }
}

