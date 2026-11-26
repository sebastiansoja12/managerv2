package com.warehouse.logisticsdelivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryStepOutcome;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.DeliveryStep;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;

public final class CourierAssignmentFixture {

    public static final ZoneId WARSAW = ZoneId.of("Europe/Warsaw");
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 11, 23, 6, 0);
    public static final LocalDateTime ARRIVED_AT = LocalDateTime.of(2026, 11, 24, 9, 15);
    public static final DepartmentAssignmentRunId RUN_ID = new DepartmentAssignmentRunId(
            java.util.UUID.fromString("4b1d7c52-2f0e-4c8a-9d4e-1c7f0a3b9e11"));
    public static final Instant TASK_CREATED_AT = Instant.parse("2026-11-24T13:00:00Z");
    public static final Courier KATOWICE_COURIER = new Courier(new SupplierId(11L), new VehicleId(21L),
            Set.of("40-001", "40-002"), 0);
    public static final Courier SECOND_KATOWICE_COURIER = new Courier(new SupplierId(12L), new VehicleId(22L),
            Set.of("40-002"), 0);

    private CourierAssignmentFixture() {
    }

    public static Delivery awaitingCourierAtDestination(final String deliveryId, final ShipmentId shipmentId) {
        return courierDelivery(deliveryId, shipmentId, DESTINATION_DEPARTMENT_ID, null, DeliveryStatus.DEPOT);
    }

    public static Delivery inTransit(final String deliveryId, final ShipmentId shipmentId) {
        return courierDelivery(deliveryId, shipmentId, ORIGIN_DEPARTMENT_ID, null, DeliveryStatus.DEPOT);
    }

    public static Delivery assignedTo(final String deliveryId, final ShipmentId shipmentId,
                                      final SupplierId supplierId) {
        return courierDelivery(deliveryId, shipmentId, DESTINATION_DEPARTMENT_ID, supplierId, DeliveryStatus.DEPOT);
    }

    public static Delivery lastScannedWith(final String deliveryId, final ShipmentId shipmentId,
                                           final DeliveryStatus lastStepStatus) {
        return courierDelivery(deliveryId, shipmentId, DESTINATION_DEPARTMENT_ID, null, lastStepStatus);
    }

    public static DepartmentAssignmentConfig config(final AssignmentWindowType windowType, final LocalTime start,
                                                    final LocalTime end) {
        return new DepartmentAssignmentConfig(DESTINATION_DEPARTMENT_ID, true,
                CourierAssignmentDateField.DESTINATION_ARRIVAL, windowType, start, end, WARSAW, Duration.ofMinutes(5),
                Duration.ofMinutes(15), Duration.ofHours(24), Duration.ofDays(3), 2);
    }

    public static DepartmentAssignmentRun processingRun() {
        return new DepartmentAssignmentRun(RUN_ID, 3L, DESTINATION_DEPARTMENT_ID,
                CourierAssignmentDateField.DESTINATION_ARRIVAL, new AssignmentWindow(
                Instant.parse("2026-11-24T07:00:00Z"), Instant.parse("2026-11-24T11:00:00Z"),
                Instant.parse("2026-11-21T13:00:00Z")), 2, DepartmentAssignmentRunStatus.PROCESSING, null, null,
                null, 0, "worker-1", Instant.parse("2026-11-24T13:05:00Z"), AssignmentRunSummary.empty(), null,
                TASK_CREATED_AT, TASK_CREATED_AT);
    }

    private static Delivery courierDelivery(final String deliveryId, final ShipmentId shipmentId,
                                            final DepartmentId lastDepartment, final SupplierId supplierId,
                                            final DeliveryStatus lastStepStatus) {
        final DeliveryId id = new DeliveryId(deliveryId);
        final DeliveryStep created = new DeliveryStep(DeliveryStepId.generate(), id, CREATED_AT,
                DeliveryStepOutcome.IN_PROGRESS, DeliveryStatus.DEPOT, null, USER_ID, null, ORIGIN_DEPARTMENT_ID,
                null, null, null, null, null, 0);
        final DeliveryStep arrived = new DeliveryStep(DeliveryStepId.generate(), id, ARRIVED_AT,
                DeliveryStepOutcome.IN_PROGRESS, lastStepStatus, null, USER_ID, null, lastDepartment, null, null,
                null, null, null, 1);
        return new Delivery(id, DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                List.of(created, arrived), CREATED_AT, supplierId, null, DeliveryStatus.DEPOT,
                DeliveryLifecycleStatus.MODIFIED, ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID, lastDepartment,
                PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null, false, null, null, null, null, null, 1L);
    }
}
