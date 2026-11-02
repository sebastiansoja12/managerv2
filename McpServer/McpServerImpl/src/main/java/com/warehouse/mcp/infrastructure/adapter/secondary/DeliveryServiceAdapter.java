package com.warehouse.mcp.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.api.DeliveryManagementApiService;
import com.warehouse.logisticsdelivery.api.dto.DeliveryConditionsChangeDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryDetailsDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryRouteChangeDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryStepDetailsDto;
import com.warehouse.mcp.application.port.secondary.DeliveryServicePort;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DeliveryRoute;
import com.warehouse.mcp.domain.vo.DeliveryStep;

public class DeliveryServiceAdapter implements DeliveryServicePort {

    private final DeliveryManagementApiService deliveryManagementApiService;

    public DeliveryServiceAdapter(final DeliveryManagementApiService deliveryManagementApiService) {
        this.deliveryManagementApiService = deliveryManagementApiService;
    }

    @Override
    public List<DeliveryDetails> findDeliveries(final DepartmentId departmentId, final SupplierId supplierId,
                                                final int limit) {
        return this.deliveryManagementApiService.findDeliveries(departmentId, supplierId, 0, limit).stream()
                .map(this::map)
                .toList();
    }

    @Override
    public DeliveryDetails findDelivery(final DeliveryId deliveryId) {
        return map(this.deliveryManagementApiService.findDelivery(deliveryId));
    }

    @Override
    public DeliveryDetails findDeliveryByShipment(final ShipmentId shipmentId) {
        return map(this.deliveryManagementApiService.findDeliveryByShipment(shipmentId));
    }

    @Override
    public DeliveryDetails assignCourier(final DeliveryId deliveryId, final SupplierId supplierId,
                                         final VehicleId vehicleId) {
        return map(this.deliveryManagementApiService.assignSupplier(deliveryId, supplierId, vehicleId));
    }

    @Override
    public DeliveryDetails unassignCourier(final DeliveryId deliveryId) {
        return map(this.deliveryManagementApiService.unassignSupplier(deliveryId));
    }

    @Override
    public DeliveryDetails changeRoute(final DeliveryId deliveryId, final DeliveryRoute route) {
        return map(this.deliveryManagementApiService.changeRoute(deliveryId, new DeliveryRouteChangeDto(
                route.originDepartmentId(), route.destinationDepartmentId(), route.pickupMethod(),
                route.pickupPointId(), route.deliveryPickupPointId())));
    }

    @Override
    public DeliveryDetails changeConditions(final DeliveryId deliveryId, final DeliveryConditions conditions) {
        return map(this.deliveryManagementApiService.changeConditions(deliveryId, new DeliveryConditionsChangeDto(
                conditions.method(), conditions.signatureRequired(), conditions.instructions(),
                conditions.plannedAt())));
    }

    @Override
    public DeliveryDetails commentStep(final DeliveryId deliveryId, final int stepNumber, final String comment) {
        return map(this.deliveryManagementApiService.commentStep(deliveryId, stepNumber, comment));
    }

    @Override
    public DeliveryDetails correctStepStatus(final DeliveryId deliveryId, final int stepNumber,
                                             final String deliveryStatus) {
        return map(this.deliveryManagementApiService.correctStepStatus(deliveryId, stepNumber, deliveryStatus));
    }

    @Override
    public void cancel(final DeliveryId deliveryId) {
        this.deliveryManagementApiService.cancelDelivery(deliveryId);
    }

    private DeliveryDetails map(final DeliveryDetailsDto delivery) {
        return new DeliveryDetails(delivery.deliveryId(), delivery.shipmentId(), delivery.type(),
                delivery.deliveryStatus(), delivery.lifecycleStatus(), delivery.originDepartmentId(),
                delivery.destinationDepartmentId(), delivery.pickupMethod(), delivery.method(),
                delivery.pickupPointId(), delivery.deliveryPickupPointId(), delivery.supplierId(),
                delivery.vehicleId(), delivery.signatureRequired(), delivery.instructions(), delivery.createdAt(),
                delivery.plannedAt(), delivery.deliveredAt(),
                delivery.steps().stream().map(this::map).toList());
    }

    private DeliveryStep map(final DeliveryStepDetailsDto step) {
        return new DeliveryStep(step.stepNumber(), step.attemptedAt(), step.outcome(), step.deliveryStatus(),
                step.supplierId(), step.comment(), step.failureReason());
    }
}
