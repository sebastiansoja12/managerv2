package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;

public record PenaltyFee(BigDecimal value, Currency currency) {
}
