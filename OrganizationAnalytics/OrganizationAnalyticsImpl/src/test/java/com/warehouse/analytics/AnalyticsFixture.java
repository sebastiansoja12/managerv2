package com.warehouse.analytics;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.DeliveryFigures;
import com.warehouse.analytics.domain.vo.RegistrationFigures;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public final class AnalyticsFixture {

    public static final DepartmentId KATOWICE_ID = new DepartmentId(10L);

    public static final DepartmentId GLIWICE_ID = new DepartmentId(20L);

    public static final DepartmentCode KATOWICE_CODE = new DepartmentCode("KAT");

    public static final DepartmentCode GLIWICE_CODE = new DepartmentCode("GLI");

    public static final AnalyticsDepartment KATOWICE = new AnalyticsDepartment(KATOWICE_ID, KATOWICE_CODE, "Katowice");

    public static final AnalyticsDepartment GLIWICE = new AnalyticsDepartment(GLIWICE_ID, GLIWICE_CODE, "Gliwice");

    public static final AnalyticsPeriod NOVEMBER = new AnalyticsPeriod(LocalDate.of(2026, 11, 1),
            LocalDate.of(2026, 11, 30));

    public static final UserId MANAGER = new UserId(7L);

    private static final AtomicLong SEQUENCE = new AtomicLong();

    private AnalyticsFixture() {
    }

    public static RegistrationFigures registrations(final DepartmentId departmentId,
                                                    final Currency currency,
                                                    final long registered,
                                                    final long canceled,
                                                    final String value) {
        return new RegistrationFigures(departmentId, currency, registered, canceled, new BigDecimal(value));
    }

    public static DeliveryFigures deliveries(final DepartmentId departmentId,
                                             final Currency currency,
                                             final long delivered,
                                             final String value) {
        return new DeliveryFigures(departmentId, currency, delivered, new BigDecimal(value));
    }

    public static DepartmentCost cost(final DepartmentCode departmentCode,
                                      final DepartmentCostCategory category,
                                      final String amount,
                                      final Currency currency) {
        return new DepartmentCost(new DepartmentCostId(SEQUENCE.incrementAndGet()), departmentCode, category,
                new BigDecimal(amount), currency, LocalDate.of(2026, 11, 10), null, MANAGER,
                Instant.parse("2026-11-10T10:00:00Z"));
    }

    public static DailyRegistrations daily(final DepartmentId departmentId, final LocalDate day, final long count) {
        return new DailyRegistrations(departmentId, day, count);
    }
}
