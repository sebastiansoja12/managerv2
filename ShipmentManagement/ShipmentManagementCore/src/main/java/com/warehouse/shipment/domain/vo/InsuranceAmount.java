package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.InsuranceType;

public final class InsuranceAmount {

    private final InsuranceType insuranceType;

    private final Double amount;

    public InsuranceAmount(final InsuranceType insuranceType, final double amount) {
        this.insuranceType = insuranceType;
        this.amount = amount;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "InsuranceType: " + insuranceType + ", Amount: " + amount;
    }
}

