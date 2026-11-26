package com.warehouse.logisticsdelivery.domain.model;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.KATOWICE_COURIER;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.SECOND_KATOWICE_COURIER;
import static org.assertj.core.api.Assertions.assertThat;

class CourierRosterTest {

    @Test
    void shouldSelectOnlyCourierWhoseDeliveryAreaCoversPostalCode() {
        final CourierRoster roster = new CourierRoster(List.of(KATOWICE_COURIER, SECOND_KATOWICE_COURIER), Map.of());

        assertThat(roster.select("40-001")).contains(KATOWICE_COURIER);
        assertThat(roster.select("00-001")).isEmpty();
    }

    @Test
    void shouldSelectLeastLoadedMatchingCourier() {
        final CourierRoster roster = new CourierRoster(List.of(KATOWICE_COURIER, SECOND_KATOWICE_COURIER),
                Map.of(KATOWICE_COURIER.supplierId(), 5L, SECOND_KATOWICE_COURIER.supplierId(), 1L));

        assertThat(roster.select("40-002")).contains(SECOND_KATOWICE_COURIER);
    }

    @Test
    void shouldBalanceAssignmentsWithinRun() {
        final CourierRoster roster = new CourierRoster(List.of(KATOWICE_COURIER, SECOND_KATOWICE_COURIER), Map.of());

        roster.registerAssignment(roster.select("40-002").orElseThrow());

        assertThat(roster.select("40-002")).contains(SECOND_KATOWICE_COURIER);
    }

    @Test
    void shouldSkipCourierAtCapacity() {
        final Courier limited = new Courier(new SupplierId(30L), new VehicleId(31L), Set.of("40-001"), 2);
        final CourierRoster roster = new CourierRoster(List.of(limited), Map.of(limited.supplierId(), 2L));

        assertThat(roster.select("40-001")).isEmpty();
    }

    @Test
    void shouldNotLeakAssignmentsFromCopy() {
        final Courier limited = new Courier(new SupplierId(30L), new VehicleId(31L), Set.of("40-001"), 1);
        final CourierRoster roster = new CourierRoster(List.of(limited), Map.of());

        roster.copy().registerAssignment(limited);

        assertThat(roster.select("40-001")).contains(limited);
    }
}
