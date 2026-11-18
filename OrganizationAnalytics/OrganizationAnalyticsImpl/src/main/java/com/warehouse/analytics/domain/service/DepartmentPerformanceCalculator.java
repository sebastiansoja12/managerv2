package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.CostBreakdown;
import com.warehouse.analytics.domain.vo.DeliveryFigures;
import com.warehouse.analytics.domain.vo.DepartmentPerformance;
import com.warehouse.analytics.domain.vo.IncidentFigures;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.analytics.domain.vo.RegistrationFigures;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepartmentPerformanceCalculator {

    private static final int PERCENT_SCALE = 2;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public PerformanceReport calculate(final AnalyticsPeriod period,
                                       final Currency requestedCurrency,
                                       final Set<DepartmentCode> selectedDepartments,
                                       final List<AnalyticsDepartment> departments,
                                       final List<RegistrationFigures> registrations,
                                       final List<DeliveryFigures> deliveries,
                                       final List<IncidentFigures> incidents,
                                       final List<DepartmentCost> costs) {
        final List<Currency> availableCurrencies = availableCurrencies(registrations, deliveries, costs);
        final Currency currency = Optional.ofNullable(requestedCurrency)
                .or(() -> dominantCurrency(registrations))
                .orElseGet(() -> availableCurrencies.isEmpty() ? null : availableCurrencies.get(0));

        final Map<DepartmentId, DepartmentCode> departmentCodes = new HashMap<>();
        final Map<DepartmentCode, String> cities = new HashMap<>();
        final Map<DepartmentCode, PerformanceAccumulator> accumulators = new TreeMap<>(
                Comparator.comparing(DepartmentCode::value));
        departments.forEach(department -> {
            departmentCodes.put(department.departmentId(), department.departmentCode());
            cities.put(department.departmentCode(), department.city());
            accumulators.put(department.departmentCode(), new PerformanceAccumulator());
        });
        final PerformanceAccumulator unassigned = new PerformanceAccumulator();

        registrations.forEach(figures -> accumulator(accumulators, unassigned,
                departmentCodes.get(figures.originDepartmentId())).addRegistration(figures, currency));
        deliveries.forEach(figures -> accumulator(accumulators, unassigned,
                departmentCodes.get(figures.originDepartmentId())).addDelivery(figures, currency));
        incidents.forEach(figures -> accumulator(accumulators, unassigned, figures.departmentCode())
                .addIncidents(figures));
        costs.stream()
                .filter(cost -> cost.currency().equals(currency))
                .forEach(cost -> accumulator(accumulators, unassigned, cost.departmentCode()).addCost(cost));

        final PerformanceAccumulator organization = new PerformanceAccumulator();
        accumulators.values().forEach(organization::merge);
        organization.merge(unassigned);

        final List<DepartmentPerformance> departmentPerformance = accumulators.entrySet().stream()
                .filter(entry -> selectedDepartments.isEmpty() || selectedDepartments.contains(entry.getKey()))
                .map(entry -> entry.getValue().toPerformance(entry.getKey(), cities.get(entry.getKey()),
                        organization.revenue))
                .toList();

        return new PerformanceReport(period, currency, availableCurrencies,
                organization.toPerformance(null, null, organization.revenue),
                unassigned.isEmpty()
                        ? Optional.empty()
                        : Optional.of(unassigned.toPerformance(null, null, organization.revenue)),
                departmentPerformance);
    }

    private PerformanceAccumulator accumulator(final Map<DepartmentCode, PerformanceAccumulator> accumulators,
                                               final PerformanceAccumulator unassigned,
                                               final DepartmentCode departmentCode) {
        return departmentCode == null ? unassigned : accumulators.getOrDefault(departmentCode, unassigned);
    }

    private List<Currency> availableCurrencies(final List<RegistrationFigures> registrations,
                                               final List<DeliveryFigures> deliveries,
                                               final List<DepartmentCost> costs) {
        return Stream.of(
                        registrations.stream().map(RegistrationFigures::currency),
                        deliveries.stream().map(DeliveryFigures::currency),
                        costs.stream().map(DepartmentCost::currency))
                .flatMap(stream -> stream)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Currency::name))
                .toList();
    }

    private Optional<Currency> dominantCurrency(final Collection<RegistrationFigures> registrations) {
        return registrations.stream()
                .filter(figures -> figures.currency() != null)
                .collect(Collectors.groupingBy(RegistrationFigures::currency,
                        Collectors.summingLong(RegistrationFigures::registeredShipments)))
                .entrySet().stream()
                .max(Map.Entry.<Currency, Long>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Currency::name).reversed())))
                .map(Map.Entry::getKey);
    }

    private static BigDecimal percent(final BigDecimal value, final BigDecimal base) {
        if (base.signum() == 0) {
            return null;
        }
        return value.multiply(HUNDRED).divide(base, PERCENT_SCALE, RoundingMode.HALF_UP);
    }

    private static final class PerformanceAccumulator {

        private long registeredShipments;

        private long canceledShipments;

        private long unpricedShipments;

        private long deliveredShipments;

        private BigDecimal revenue = BigDecimal.ZERO;

        private BigDecimal actualTurnover = BigDecimal.ZERO;

        private CostBreakdown costs = CostBreakdown.NONE;

        private long costEntries;

        private long damagedShipments;

        private long damageClaims;

        void addRegistration(final RegistrationFigures figures, final Currency currency) {
            registeredShipments += figures.registeredShipments();
            canceledShipments += figures.canceledShipments();
            if (figures.currency() == null) {
                unpricedShipments += figures.registeredShipments();
            } else if (figures.currency().equals(currency)) {
                revenue = revenue.add(figures.registeredValue());
            }
        }

        void addDelivery(final DeliveryFigures figures, final Currency currency) {
            deliveredShipments += figures.deliveredShipments();
            if (figures.currency() != null && figures.currency().equals(currency)) {
                actualTurnover = actualTurnover.add(figures.deliveredValue());
            }
        }

        void addIncidents(final IncidentFigures figures) {
            damagedShipments += figures.damagedShipments();
            damageClaims += figures.damageClaims();
        }

        void addCost(final DepartmentCost cost) {
            costs = costs.plus(CostBreakdown.of(cost.category(), cost.amount()));
            costEntries++;
        }

        void merge(final PerformanceAccumulator other) {
            registeredShipments += other.registeredShipments;
            canceledShipments += other.canceledShipments;
            unpricedShipments += other.unpricedShipments;
            deliveredShipments += other.deliveredShipments;
            revenue = revenue.add(other.revenue);
            actualTurnover = actualTurnover.add(other.actualTurnover);
            costs = costs.plus(other.costs);
            costEntries += other.costEntries;
            damagedShipments += other.damagedShipments;
            damageClaims += other.damageClaims;
        }

        boolean isEmpty() {
            return registeredShipments == 0 && canceledShipments == 0 && deliveredShipments == 0
                    && costEntries == 0 && damagedShipments == 0 && damageClaims == 0;
        }

        DepartmentPerformance toPerformance(final DepartmentCode departmentCode,
                                            final String city,
                                            final BigDecimal organizationRevenue) {
            final BigDecimal financialResult = revenue.subtract(costs.total());
            return new DepartmentPerformance(departmentCode, city, registeredShipments, canceledShipments,
                    unpricedShipments, deliveredShipments, revenue, actualTurnover, costs, costEntries,
                    financialResult, percent(financialResult, revenue), percent(revenue, organizationRevenue),
                    damagedShipments, damageClaims);
        }
    }
}
