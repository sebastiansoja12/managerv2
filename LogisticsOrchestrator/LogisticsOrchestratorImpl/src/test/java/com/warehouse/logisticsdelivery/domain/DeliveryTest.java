package com.warehouse.logisticsdelivery.domain;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryCommentCode;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryStepOutcome;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryStepNotFoundException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryStepSnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import org.junit.jupiter.api.Test;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DELIVERY_PICKUP_POINT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.PICKUP_POINT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryTest {

    @Test
    void shouldKeepRouteOnDeliveryAndNotOnCreatedStep() {
        final DeliverySnapshot delivery = shipmentDelivery(new ShipmentId(10L)).snapshot();

        assertThat(delivery.originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(delivery.destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(delivery.pickupMethod()).isEqualTo(PickupMethod.PICKUP_POINT);
        assertThat(delivery.method()).isEqualTo(DeliveryMethod.LOCKER);
        assertThat(delivery.pickupPointId()).isEqualTo(PICKUP_POINT_ID);
        assertThat(delivery.deliveryPickupPointId()).isEqualTo(DELIVERY_PICKUP_POINT_ID);
        assertThat(delivery.deliverySteps()).hasSize(1);
        assertThat(delivery.deliverySteps().get(0).departmentId()).isNull();
    }

    @Test
    void shouldChangeDestinationDepartmentWhenDeliveryIsActive() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        final DepartmentId newDestinationDepartmentId = new DepartmentId(8L);

        final boolean changed = delivery.changeDestinationDepartment(newDestinationDepartmentId);

        assertThat(changed).isTrue();
        assertThat(delivery.getDestinationDepartmentId()).isEqualTo(newDestinationDepartmentId);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.MODIFIED);
    }

    @Test
    void shouldNotReportChangeWhenDestinationDepartmentIsTheSame() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        final boolean changed = delivery.changeDestinationDepartment(DESTINATION_DEPARTMENT_ID);

        assertThat(changed).isFalse();
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CREATED);
    }

    @Test
    void shouldThrowExceptionWhenChangingDestinationDepartmentOfCompletedDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        assertThatThrownBy(() -> delivery.changeDestinationDepartment(new DepartmentId(8L)))
                .isInstanceOf(DeliveryModificationException.class);
    }

    @Test
    void shouldThrowExceptionWhenCompletingCompletedDeliveryAgain() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        assertThatThrownBy(() -> delivery.complete(null, USER_ID, null))
                .isInstanceOf(DeliveryModificationException.class);
        assertThat(delivery.getDeliverySteps()).hasSize(2);
    }

    @Test
    void shouldRecordAttemptAsNextStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        delivery.recordAttempt(DeliveryStatus.REJECTED, USER_ID, new SupplierId(4L), new DepartmentId(7L), null,
                "Recipient absent", null);

        final DeliveryStepSnapshot attempt = delivery.snapshot().deliverySteps().get(1);
        assertThat(attempt.stepNumber()).isEqualTo(2);
        assertThat(attempt.outcome()).isEqualTo(DeliveryStepOutcome.FAILED);
        assertThat(attempt.departmentId()).isEqualTo(new DepartmentId(7L));
        assertThat(attempt.failureReason()).isEqualTo("Recipient absent");
    }

    @Test
    void shouldCompleteDeliveryWithFinalStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        delivery.complete(new SupplierId(4L), USER_ID, new DepartmentId(7L));

        final DeliverySnapshot snapshot = delivery.snapshot();
        assertThat(snapshot.status()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
        assertThat(snapshot.deliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        assertThat(snapshot.deliveredAt()).isNotNull();
        assertThat(snapshot.deliverySteps()).hasSize(2);
        assertThat(snapshot.deliverySteps().get(1).outcome()).isEqualTo(DeliveryStepOutcome.SUCCEEDED);
    }

    @Test
    void shouldCopyRouteForRedirectedDelivery() {
        final Delivery source = shipmentDelivery(new ShipmentId(10L));

        final DeliverySnapshot derived = Delivery.derivedFrom(source, new ShipmentId(11L),
                DeliveryDerivationType.REDIRECT, false, USER_ID).snapshot();

        assertThat(derived.target()).isEqualTo(DeliveryTarget.shipment(new ShipmentId(11L)));
        assertThat(derived.originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(derived.destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(derived.pickupMethod()).isEqualTo(PickupMethod.PICKUP_POINT);
        assertThat(derived.method()).isEqualTo(DeliveryMethod.LOCKER);
        assertThat(derived.pickupPointId()).isEqualTo(PICKUP_POINT_ID);
        assertThat(derived.deliveryPickupPointId()).isEqualTo(DELIVERY_PICKUP_POINT_ID);
    }

    @Test
    void shouldReverseRouteForReturnedDelivery() {
        final Delivery source = shipmentDelivery(new ShipmentId(10L));

        final DeliverySnapshot derived = Delivery.derivedFrom(source, new ShipmentId(11L),
                DeliveryDerivationType.RETURN, false, USER_ID).snapshot();

        assertThat(derived.originDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(derived.destinationDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(derived.pickupMethod()).isEqualTo(PickupMethod.DEPARTMENT);
        assertThat(derived.method()).isEqualTo(DeliveryMethod.COURIER);
        assertThat(derived.pickupPointId()).isNull();
        assertThat(derived.deliveryPickupPointId()).isNull();
    }

    @Test
    void shouldNotReplaceInactiveDeliveryByPalletDelivery() {
        final Delivery replaced = shipmentDelivery(new ShipmentId(10L));
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("p"), USER_ID);
        replaced.replaceByPalletDelivery(palletDelivery.getDeliveryId(), USER_ID);

        assertThat(replaced.isActive()).isFalse();
        assertThat(replaced.getReplacedByDeliveryId()).isEqualTo(palletDelivery.getDeliveryId());
        final DeliveryStepSnapshot replacementStep = replaced.snapshot().deliverySteps().getLast();
        assertThat(replacementStep.commentCode()).isEqualTo(DeliveryCommentCode.REPLACED_BY_PALLET_DELIVERY);
        assertThat(replacementStep.comment()).isNull();
        assertThat(replaced.replaceByPalletDelivery(replaced.getDeliveryId(), USER_ID)).isFalse();
    }

    @Test
    void shouldAssignSupplierAndVehicleToActiveDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        final boolean changed = delivery.assignSupplier(new SupplierId(4L), new VehicleId(9L));

        assertThat(changed).isTrue();
        assertThat(delivery.getSupplierId()).isEqualTo(new SupplierId(4L));
        assertThat(delivery.getVehicleId()).isEqualTo(new VehicleId(9L));
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.MODIFIED);
    }

    @Test
    void shouldChangeConditionsOfDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        final boolean changed = delivery.changeConditions(DeliveryMethod.COURIER, true, "Call before", null);

        final DeliverySnapshot snapshot = delivery.snapshot();
        assertThat(changed).isTrue();
        assertThat(snapshot.method()).isEqualTo(DeliveryMethod.COURIER);
        assertThat(snapshot.signatureRequired()).isTrue();
        assertThat(snapshot.instructions()).isEqualTo("Call before");
    }

    @Test
    void shouldCommentStepOfCompletedDeliveryWithoutChangingLifecycle() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        final boolean changed = delivery.commentStep(2, "Left at reception");

        assertThat(changed).isTrue();
        assertThat(delivery.snapshot().deliverySteps().get(1).comment()).isEqualTo("Left at reception");
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
    }

    @Test
    void shouldCorrectStepStatusAndRecomputeOutcome() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.recordAttempt(DeliveryStatus.REJECTED, USER_ID, null, null, null, "Recipient absent", null);

        final boolean changed = delivery.correctStepStatus(2, DeliveryStatus.DELIVERED);

        final DeliveryStepSnapshot step = delivery.snapshot().deliverySteps().get(1);
        assertThat(changed).isTrue();
        assertThat(step.outcome()).isEqualTo(DeliveryStepOutcome.SUCCEEDED);
        assertThat(step.failureReason()).isNull();
    }

    @Test
    void shouldAttachSignatureOnlyToSucceededStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.recordAttempt(DeliveryStatus.REJECTED, USER_ID, null, null, null, null, null);
        delivery.recordAttempt(DeliveryStatus.DELIVERED, USER_ID, null, null, null, null, null);

        assertThatThrownBy(() -> delivery.attachStepSignature(2, new SignatureId(11L)))
                .isInstanceOf(DeliveryModificationException.class);
        assertThat(delivery.attachStepSignature(3, new SignatureId(11L))).isTrue();
        assertThat(delivery.snapshot().deliverySteps().get(2).signatureId()).isEqualTo(new SignatureId(11L));
    }

    @Test
    void shouldReassignCarrierOfStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.recordAttempt(DeliveryStatus.DELIVERY, USER_ID, null, null, null, null, null);

        final boolean changed = delivery.reassignStepCarrier(2, new SupplierId(4L), new VehicleId(9L));

        final DeliveryStepSnapshot step = delivery.snapshot().deliverySteps().get(1);
        assertThat(changed).isTrue();
        assertThat(step.supplierId()).isEqualTo(new SupplierId(4L));
        assertThat(step.vehicleId()).isEqualTo(new VehicleId(9L));
    }

    @Test
    void shouldThrowExceptionWhenStepDoesNotExist() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        assertThatThrownBy(() -> delivery.commentStep(5, "comment"))
                .isInstanceOf(DeliveryStepNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenCorrectingStepOfCompletedDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        assertThatThrownBy(() -> delivery.correctStepStatus(2, DeliveryStatus.REJECTED))
                .isInstanceOf(DeliveryModificationException.class);
    }
}
