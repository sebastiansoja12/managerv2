package com.warehouse.logisticsdelivery.domain;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import org.junit.jupiter.api.Test;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.ARRIVED_AT;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.assignedTo;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.awaitingCourierAtDestination;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.inTransit;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.lastScannedWith;
import static com.warehouse.logisticsdelivery.DeliveryFixture.courierShipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryCourierAssignmentTest {

    private static final SupplierId SUPPLIER_ID = new SupplierId(11L);
    private static final VehicleId VEHICLE_ID = new VehicleId(21L);

    @Test
    void shouldAssignCourierWhenDeliveryAwaitsCourierAtDestination() {
        final Delivery delivery = awaitingCourierAtDestination("d-1", new ShipmentId(1L));

        delivery.assignCourier(SUPPLIER_ID, VEHICLE_ID);

        assertThat(delivery.getSupplierId()).isEqualTo(SUPPLIER_ID);
        assertThat(delivery.getVehicleId()).isEqualTo(VEHICLE_ID);
        assertThat(delivery.isAwaitingCourierAtDestination()).isFalse();
    }

    @Test
    void shouldKeepExistingCourierAssignment() {
        final SupplierId existingSupplier = new SupplierId(99L);
        final Delivery delivery = assignedTo("d-1", new ShipmentId(1L), existingSupplier);

        delivery.assignCourier(SUPPLIER_ID, VEHICLE_ID);

        assertThat(delivery.getSupplierId()).isEqualTo(existingSupplier);
    }

    @Test
    void shouldRejectCourierAssignmentWhenDeliveryIsNotInDestinationDepartment() {
        final Delivery delivery = inTransit("d-1", new ShipmentId(1L));

        assertThatThrownBy(() -> delivery.assignCourier(SUPPLIER_ID, VEHICLE_ID))
                .isInstanceOf(DeliveryModificationException.class);
        assertThat(delivery.getSupplierId()).isNull();
    }

    @Test
    void shouldNotAwaitCourierWhenLastScanIsNotDepot() {
        final Delivery delivery = lastScannedWith("d-1", new ShipmentId(1L), DeliveryStatus.RETURN);

        assertThat(delivery.isAwaitingCourierAtDestination()).isFalse();
    }

    @Test
    void shouldResolveArrivalTimeInCurrentDepartment() {
        final Delivery delivery = awaitingCourierAtDestination("d-1", new ShipmentId(1L));

        assertThat(delivery.getCurrentDepartmentSince()).isEqualTo(ARRIVED_AT);
    }

    @Test
    void shouldUseCreationTimeWhenNoDepartmentWasScanned() {
        final Delivery delivery = courierShipmentDelivery(new ShipmentId(1L));

        assertThat(delivery.getCurrentDepartmentSince()).isEqualTo(delivery.getCreatedAt());
    }
}
