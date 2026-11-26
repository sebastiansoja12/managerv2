package com.warehouse.commonassets.model;

import com.warehouse.commonassets.enumeration.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void shouldAddWithoutMutatingOperands() {
        final Money left = Money.from(new BigDecimal("10.10"), Currency.PLN);
        final Money right = Money.from(new BigDecimal("2.205"), Currency.PLN);

        final Money sum = left.add(right);

        assertThat(sum.getAmount()).isEqualTo(new BigDecimal("12.31"));
        assertThat(sum.getCurrency()).isEqualTo(Currency.PLN);
        assertThat(left.getAmount()).isEqualTo(new BigDecimal("10.10"));
        assertThat(right.getAmount()).isEqualTo(new BigDecimal("2.205"));
    }

    @Test
    void shouldSubtract() {
        final Money result = Money.from(new BigDecimal("10.00"), Currency.PLN)
                .subtract(Money.from(new BigDecimal("12.50"), Currency.PLN));

        assertThat(result.getAmount()).isEqualTo(new BigDecimal("-2.50"));
    }

    @Test
    void shouldMultiplyAndRoundHalfUp() {
        final Money result = Money.from(new BigDecimal("10.00"), Currency.PLN).multiply(new BigDecimal("0.125"));

        assertThat(result.getAmount()).isEqualTo(new BigDecimal("1.25"));
        assertThat(Money.from(new BigDecimal("0.05"), Currency.PLN).multiply(new BigDecimal("0.5")).getAmount())
                .isEqualTo(new BigDecimal("0.03"));
    }

    @Test
    void shouldCalculatePercentage() {
        final Money result = Money.from(new BigDecimal("14.00"), Currency.PLN).percentage(new BigDecimal("23"));

        assertThat(result.getAmount()).isEqualTo(new BigDecimal("3.22"));
        assertThat(Money.from(new BigDecimal("0.10"), Currency.EUR).percentage(new BigDecimal("5")).getAmount())
                .isEqualTo(new BigDecimal("0.01"));
    }

    @Test
    void shouldNegate() {
        assertThat(Money.from(new BigDecimal("4.20"), Currency.PLN).negate().getAmount())
                .isEqualTo(new BigDecimal("-4.20"));
    }

    @Test
    void shouldReportZeroAndPositive() {
        assertThat(Money.from(BigDecimal.ZERO, Currency.PLN).isZero()).isTrue();
        assertThat(Money.from(new BigDecimal("0.00"), Currency.PLN).isPositive()).isFalse();
        assertThat(Money.from(new BigDecimal("0.01"), Currency.PLN).isPositive()).isTrue();
        assertThat(Money.from(new BigDecimal("-0.01"), Currency.PLN).isPositive()).isFalse();
    }

    @Test
    void shouldCompareAmountsOfSameCurrency() {
        final Money small = Money.from(new BigDecimal("1.0"), Currency.PLN);
        final Money big = Money.from(new BigDecimal("2.00"), Currency.PLN);

        assertThat(small.compareTo(big)).isNegative();
        assertThat(big.compareTo(small)).isPositive();
        assertThat(small.compareTo(Money.from(new BigDecimal("1.00"), Currency.PLN))).isZero();
    }

    @Test
    void shouldRejectMixedCurrencies() {
        final Money pln = Money.from(BigDecimal.ONE, Currency.PLN);
        final Money eur = Money.from(BigDecimal.ONE, Currency.EUR);

        assertThatThrownBy(() -> pln.add(eur)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pln.subtract(eur)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pln.compareTo(eur)).isInstanceOf(IllegalArgumentException.class);
    }
}
