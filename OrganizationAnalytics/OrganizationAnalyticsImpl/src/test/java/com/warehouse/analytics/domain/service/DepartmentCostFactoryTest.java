package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.commonassets.enumeration.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_CODE;
import static com.warehouse.analytics.AnalyticsFixture.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepartmentCostFactoryTest {

    private static final Instant NOW = Instant.parse("2026-11-17T10:00:00Z");

    private static final DepartmentCostFactory FACTORY = new DepartmentCostFactory(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldRecordCostWithNormalizedAmountAndDescription() {
        final DepartmentCost cost = record("125.5", LocalDate.of(2026, 11, 16), "  Fuel invoice 11/2026  ");

        assertThat(cost.costId()).isNotNull();
        assertThat(cost.amount()).isEqualTo(new BigDecimal("125.50"));
        assertThat(cost.description()).isEqualTo("Fuel invoice 11/2026");
        assertThat(cost.recordedBy()).isEqualTo(MANAGER);
        assertThat(cost.recordedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldStoreBlankDescriptionAsMissing() {
        assertThat(record("10", LocalDate.of(2026, 11, 17), "   ").description()).isNull();
    }

    @Test
    void shouldRejectNonPositiveAmount() {
        assertInvalid(() -> record("0", LocalDate.of(2026, 11, 16), null));
        assertInvalid(() -> record("-5.00", LocalDate.of(2026, 11, 16), null));
    }

    @Test
    void shouldRejectAmountWithMoreThanTwoDecimalPlaces() {
        assertInvalid(() -> record("10.005", LocalDate.of(2026, 11, 16), null));
    }

    @Test
    void shouldRejectCostIncurredInTheFuture() {
        assertInvalid(() -> record("10.00", LocalDate.of(2026, 11, 18), null));
    }

    @Test
    void shouldRejectTooLongDescription() {
        assertInvalid(() -> record("10.00", LocalDate.of(2026, 11, 16), "x".repeat(501)));
    }

    @Test
    void shouldRejectInvalidAnalyticsPeriod() {
        assertInvalid(() -> new AnalyticsPeriod(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 1)));
        assertInvalid(() -> new AnalyticsPeriod(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 2)));
        assertThat(new AnalyticsPeriod(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31))
                .contains(LocalDate.of(2026, 6, 1))).isTrue();
    }

    private static DepartmentCost record(final String amount, final LocalDate incurredOn, final String description) {
        return FACTORY.create(KATOWICE_CODE, DepartmentCostCategory.FUEL, new BigDecimal(amount),
                Currency.PLN, incurredOn, description, MANAGER);
    }

    private static void assertInvalid(final org.assertj.core.api.ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable)
                .isInstanceOfSatisfying(AnalyticsException.class,
                        exception -> assertThat(exception.reason()).isEqualTo(AnalyticsException.Reason.INVALID_REQUEST));
    }
}
