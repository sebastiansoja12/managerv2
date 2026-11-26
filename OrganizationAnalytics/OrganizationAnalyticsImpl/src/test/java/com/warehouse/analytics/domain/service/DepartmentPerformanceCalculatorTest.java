package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.vo.DepartmentPerformance;
import com.warehouse.analytics.domain.vo.IncidentFigures;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.commonassets.enumeration.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.warehouse.analytics.AnalyticsFixture.GLIWICE;
import static com.warehouse.analytics.AnalyticsFixture.GLIWICE_CODE;
import static com.warehouse.analytics.AnalyticsFixture.GLIWICE_ID;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_CODE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_ID;
import static com.warehouse.analytics.AnalyticsFixture.NOVEMBER;
import static com.warehouse.analytics.AnalyticsFixture.cost;
import static com.warehouse.analytics.AnalyticsFixture.deliveries;
import static com.warehouse.analytics.AnalyticsFixture.registrations;
import static org.assertj.core.api.Assertions.assertThat;

class DepartmentPerformanceCalculatorTest {

    private final DepartmentPerformanceCalculator calculator = new DepartmentPerformanceCalculator();

    @Test
    void shouldCalculateRevenueTurnoverCostsAndProfitabilityPerDepartment() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, Currency.PLN, Set.of(),
                List.of(KATOWICE, GLIWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 10, 2, "1000.00"),
                        registrations(GLIWICE_ID, Currency.PLN, 5, 0, "500.00")),
                List.of(deliveries(KATOWICE_ID, Currency.PLN, 8, "800.00")),
                List.of(new IncidentFigures(KATOWICE_CODE, 3, 1)),
                List.of(cost(KATOWICE_CODE, DepartmentCostCategory.FUEL, "150.00", Currency.PLN),
                        cost(KATOWICE_CODE, DepartmentCostCategory.SHIPMENT_DAMAGE, "40.00", Currency.PLN),
                        cost(KATOWICE_CODE, DepartmentCostCategory.CLAIM, "10.00", Currency.PLN)));

        final DepartmentPerformance katowice = department(report, "KAT");

        assertThat(katowice.registeredShipments()).isEqualTo(10);
        assertThat(katowice.canceledShipments()).isEqualTo(2);
        assertThat(katowice.deliveredShipments()).isEqualTo(8);
        assertThat(katowice.revenue()).isEqualByComparingTo("1000.00");
        assertThat(katowice.actualTurnover()).isEqualByComparingTo("800.00");
        assertThat(katowice.costs().fuel()).isEqualByComparingTo("150.00");
        assertThat(katowice.costs().shipmentDamage()).isEqualByComparingTo("40.00");
        assertThat(katowice.costs().claims()).isEqualByComparingTo("10.00");
        assertThat(katowice.costs().total()).isEqualByComparingTo("200.00");
        assertThat(katowice.costEntries()).isEqualTo(3);
        assertThat(katowice.financialResult()).isEqualByComparingTo("800.00");
        assertThat(katowice.profitabilityPercent()).isEqualByComparingTo("80.00");
        assertThat(katowice.revenueSharePercent()).isEqualByComparingTo("66.67");
        assertThat(katowice.damagedShipments()).isEqualTo(3);
        assertThat(katowice.damageClaims()).isEqualTo(1);
        assertThat(department(report, "GLI").costEntries()).isZero();
        assertThat(department(report, "GLI").profitabilityPercent()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldKeepOrganizationTotalsEqualToSumOfDepartmentsAndUnassigned() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, Currency.PLN, Set.of(),
                List.of(KATOWICE, GLIWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 10, 1, "1000.00"),
                        registrations(GLIWICE_ID, Currency.PLN, 4, 0, "300.00"),
                        registrations(null, Currency.PLN, 2, 0, "50.00")),
                List.of(deliveries(GLIWICE_ID, Currency.PLN, 3, "200.00"),
                        deliveries(null, Currency.PLN, 1, "25.00")),
                List.of(new IncidentFigures(GLIWICE_CODE, 1, 0)),
                List.of(cost(GLIWICE_CODE, DepartmentCostCategory.FUEL, "100.00", Currency.PLN)));

        final DepartmentPerformance organization = report.organization();
        final DepartmentPerformance unassigned = report.unassigned().orElseThrow();

        assertThat(organization.registeredShipments()).isEqualTo(
                sumRegistered(report.departments()) + unassigned.registeredShipments()).isEqualTo(16);
        assertThat(organization.revenue()).isEqualByComparingTo(
                sumRevenue(report.departments()).add(unassigned.revenue())).isEqualByComparingTo("1350.00");
        assertThat(organization.actualTurnover()).isEqualByComparingTo("225.00");
        assertThat(organization.costs().total()).isEqualByComparingTo("100.00");
        assertThat(organization.financialResult()).isEqualByComparingTo("1250.00");
        assertThat(organization.revenueSharePercent()).isEqualByComparingTo("100.00");
        assertThat(unassigned.registeredShipments()).isEqualTo(2);
        assertThat(organization.damagedShipments()).isEqualTo(1);
    }

    @Test
    void shouldExcludeOtherCurrenciesFromAmountsAndReportUnpricedShipments() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, Currency.PLN, Set.of(),
                List.of(KATOWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 3, 0, "300.00"),
                        registrations(KATOWICE_ID, Currency.EUR, 2, 0, "40.00"),
                        registrations(KATOWICE_ID, null, 1, 0, "0")),
                List.of(),
                List.of(),
                List.of(cost(KATOWICE_CODE, DepartmentCostCategory.FUEL, "20.00", Currency.EUR)));

        final DepartmentPerformance katowice = department(report, "KAT");

        assertThat(report.currency()).isEqualTo(Currency.PLN);
        assertThat(report.availableCurrencies()).containsExactly(Currency.EUR, Currency.PLN);
        assertThat(katowice.registeredShipments()).isEqualTo(6);
        assertThat(katowice.unpricedShipments()).isEqualTo(1);
        assertThat(katowice.revenue()).isEqualByComparingTo("300.00");
        assertThat(katowice.costEntries()).isZero();
        assertThat(katowice.costs().total()).isEqualByComparingTo("0");
    }

    @Test
    void shouldSelectCurrencyWithMostRegisteredShipmentsWhenNotRequested() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, null, Set.of(), List.of(KATOWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 3, 0, "300.00"),
                        registrations(KATOWICE_ID, Currency.EUR, 7, 0, "70.00")),
                List.of(), List.of(), List.of());

        assertThat(report.currency()).isEqualTo(Currency.EUR);
        assertThat(report.organization().revenue()).isEqualByComparingTo("70.00");
    }

    @Test
    void shouldReturnZeroFiguresWithoutProfitabilityWhenThereIsNoData() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, null, Set.of(), List.of(KATOWICE, GLIWICE),
                List.of(), List.of(), List.of(), List.of());

        assertThat(report.currency()).isNull();
        assertThat(report.availableCurrencies()).isEmpty();
        assertThat(report.unassigned()).isEmpty();
        assertThat(report.departments()).hasSize(2).allSatisfy(performance -> {
            assertThat(performance.registeredShipments()).isZero();
            assertThat(performance.revenue()).isEqualByComparingTo("0");
            assertThat(performance.profitabilityPercent()).isNull();
            assertThat(performance.revenueSharePercent()).isNull();
        });
        assertThat(report.organization().profitabilityPercent()).isNull();
    }

    @Test
    void shouldReportNegativeResultWhenCostsExceedRevenue() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, Currency.PLN, Set.of(), List.of(KATOWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 1, 0, "100.00")),
                List.of(), List.of(),
                List.of(cost(KATOWICE_CODE, DepartmentCostCategory.CLAIM, "250.00", Currency.PLN)));

        assertThat(department(report, "KAT").financialResult()).isEqualByComparingTo("-150.00");
        assertThat(department(report, "KAT").profitabilityPercent()).isEqualByComparingTo("-150.00");
    }

    @Test
    void shouldFilterDepartmentRowsButKeepWholeOrganizationTotals() {
        final PerformanceReport report = calculator.calculate(NOVEMBER, Currency.PLN, Set.of(GLIWICE_CODE),
                List.of(KATOWICE, GLIWICE),
                List.of(registrations(KATOWICE_ID, Currency.PLN, 10, 0, "1000.00"),
                        registrations(GLIWICE_ID, Currency.PLN, 5, 0, "500.00")),
                List.of(), List.of(), List.of());

        assertThat(report.departments()).extracting(performance -> performance.departmentCode().value())
                .containsExactly("GLI");
        assertThat(report.organization().revenue()).isEqualByComparingTo("1500.00");
        assertThat(report.departments().get(0).revenueSharePercent()).isEqualByComparingTo("33.33");
    }

    private static DepartmentPerformance department(final PerformanceReport report, final String code) {
        return report.departments().stream()
                .filter(performance -> performance.departmentCode().value().equals(code))
                .findFirst()
                .orElseThrow();
    }

    private static long sumRegistered(final List<DepartmentPerformance> departments) {
        return departments.stream().mapToLong(DepartmentPerformance::registeredShipments).sum();
    }

    private static BigDecimal sumRevenue(final List<DepartmentPerformance> departments) {
        return departments.stream().map(DepartmentPerformance::revenue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
