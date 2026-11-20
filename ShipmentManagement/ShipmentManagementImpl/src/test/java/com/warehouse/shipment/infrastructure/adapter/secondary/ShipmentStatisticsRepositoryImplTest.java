package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentDeliveryFigures;
import com.warehouse.shipment.application.port.primary.result.ShipmentRegistrationFigures;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentStatisticsFixture.GLIWICE;
import static com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentStatisticsFixture.KATOWICE;
import static com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentStatisticsFixture.OPERATOR;
import static com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentStatisticsFixture.OTHER_OPERATOR;
import static com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentStatisticsFixture.shipment;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = ShipmentStatisticsRepositoryImplTest.ShipmentStatisticsTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShipmentStatisticsRepositoryImplTest {

    private static final LocalDateTime RANGE_START = LocalDate.of(2026, 11, 1).atStartOfDay();

    private static final LocalDateTime RANGE_END = LocalDate.of(2026, 11, 8).atStartOfDay();

    @AutoConfigurationPackage(basePackageClasses = ShipmentReadEntity.class)
    static class ShipmentStatisticsTestConfiguration {
    }

    @PersistenceContext
    private EntityManager entityManager;

    private ShipmentStatisticsRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        final OperatorContextProvider operatorContextProvider = () -> Optional.of(
                new OperatorDetails(OPERATOR, new UserId(1L), KATOWICE));
        this.repository = new ShipmentStatisticsRepositoryImpl(
                new BaseRepository<>(this.entityManager, operatorContextProvider));

        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.SENT, at(2, 9), null, "20.00", Currency.PLN));
        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.DELIVERY, at(2, 15), at(4, 12), "30.50", Currency.PLN));
        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.CANCELED, at(3, 10), null, "99.00", Currency.PLN));
        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.CREATED, at(3, 11), null, null, null));
        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.PLANNED, at(3, 12), null, "50.00", Currency.PLN));
        persist(shipment(OPERATOR, GLIWICE, ShipmentStatus.SENT, at(5, 8), null, "15.00", Currency.EUR));
        persist(shipment(OPERATOR, null, ShipmentStatus.SENT, at(6, 8), null, "10.00", Currency.PLN));
        persist(shipment(OPERATOR, KATOWICE, ShipmentStatus.SENT, at(9, 8), null, "70.00", Currency.PLN));
        persist(shipment(OPERATOR, GLIWICE, ShipmentStatus.DELIVERY, LocalDateTime.of(2026, 10, 20, 8, 0),
                at(3, 9), "40.00", Currency.PLN));
        persist(shipment(OTHER_OPERATOR, KATOWICE, ShipmentStatus.SENT, at(2, 9), null, "500.00", Currency.PLN));
        this.entityManager.flush();
    }

    @Test
    void shouldAggregateRegisteredShipmentsByOriginDepartmentAndCurrency() {
        final List<ShipmentRegistrationFigures> figures = this.repository.registrationFigures(RANGE_START,
                RANGE_END).stream()
                .map(figure -> new ShipmentRegistrationFigures(figure.originDepartmentId(), figure.currency(),
                        figure.registeredShipments(), figure.canceledShipments(), scaled(figure.registeredValue())))
                .toList();

        assertThat(figures)
                .containsExactlyInAnyOrder(
                        new ShipmentRegistrationFigures(KATOWICE, Currency.PLN, 2, 1, new BigDecimal("50.50")),
                        new ShipmentRegistrationFigures(KATOWICE, null, 1, 0, new BigDecimal("0.00")),
                        new ShipmentRegistrationFigures(GLIWICE, Currency.EUR, 1, 0, new BigDecimal("15.00")),
                        new ShipmentRegistrationFigures(null, Currency.PLN, 1, 0, new BigDecimal("10.00")));
    }

    @Test
    void shouldAggregateDeliveriesByDeliveryDate() {
        final List<ShipmentDeliveryFigures> figures = this.repository.deliveryFigures(RANGE_START, RANGE_END)
                .stream()
                .map(figure -> new ShipmentDeliveryFigures(figure.originDepartmentId(), figure.currency(),
                        figure.deliveredShipments(), scaled(figure.deliveredValue())))
                .toList();

        assertThat(figures)
                .containsExactlyInAnyOrder(
                        new ShipmentDeliveryFigures(KATOWICE, Currency.PLN, 1, new BigDecimal("30.50")),
                        new ShipmentDeliveryFigures(GLIWICE, Currency.PLN, 1, new BigDecimal("40.00")));
    }

    @Test
    void shouldCountDailyRegistrationsWithoutCanceledAndPlannedShipments() {
        final List<DailyShipmentCount> counts = this.repository.dailyRegistrations(RANGE_START, RANGE_END);

        assertThat(counts).containsExactlyInAnyOrder(
                new DailyShipmentCount(KATOWICE, LocalDate.of(2026, 11, 2), 2),
                new DailyShipmentCount(KATOWICE, LocalDate.of(2026, 11, 3), 1),
                new DailyShipmentCount(GLIWICE, LocalDate.of(2026, 11, 5), 1),
                new DailyShipmentCount(null, LocalDate.of(2026, 11, 6), 1));
    }

    @Test
    void shouldFindFirstRegistrationOfCurrentOperator() {
        assertThat(this.repository.firstRegistration()).contains(LocalDateTime.of(2026, 10, 20, 8, 0));
    }

    private void persist(final ShipmentReadEntity entity) {
        this.entityManager.persist(entity);
    }

    private static BigDecimal scaled(final BigDecimal amount) {
        return amount.setScale(2);
    }

    private static LocalDateTime at(final int dayOfNovember, final int hour) {
        return LocalDateTime.of(2026, 11, dayOfNovember, hour, 0);
    }
}
