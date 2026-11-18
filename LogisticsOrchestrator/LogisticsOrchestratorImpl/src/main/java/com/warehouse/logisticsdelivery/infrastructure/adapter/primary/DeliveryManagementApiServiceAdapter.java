package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

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
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryConditionsRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryRouteRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepCommentRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepResponseDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepStatusRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliverySupplierRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;

public class DeliveryManagementApiServiceAdapter implements DeliveryManagementApiService {

    private final DeliveryPort deliveryPort;

    private final DeliveryRequestMapper requestMapper;

    private final DeliveryResponseMapper responseMapper;

    public DeliveryManagementApiServiceAdapter(final DeliveryPort deliveryPort,
                                               final DeliveryRequestMapper requestMapper,
                                               final DeliveryResponseMapper responseMapper) {
        this.deliveryPort = deliveryPort;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public List<DeliveryDetailsDto> findDeliveries(final DepartmentId departmentId, final SupplierId supplierId,
                                                   final int offset, final int limit) {
        final List<DeliverySnapshot> deliveries;
        if (supplierId != null) {
            deliveries = this.deliveryPort.findDeliveriesBySupplier(supplierId, offset, limit);
        } else if (departmentId != null) {
            deliveries = this.deliveryPort.findDeliveriesByDepartment(departmentId, offset, limit);
        } else {
            deliveries = this.deliveryPort.findRecentDeliveries(offset, limit);
        }
        return deliveries.stream().map(this::map).toList();
    }

    @Override
    public DeliveryDetailsDto findDelivery(final DeliveryId deliveryId) {
        return map(this.deliveryPort.findDelivery(deliveryId));
    }

    @Override
    public DeliveryDetailsDto findDeliveryByShipment(final ShipmentId shipmentId) {
        return map(this.deliveryPort.findDeliveryByShipmentId(shipmentId));
    }

    @Override
    public DeliveryDetailsDto assignSupplier(final DeliveryId deliveryId, final SupplierId supplierId,
                                             final VehicleId vehicleId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier is required");
        }
        return map(this.deliveryPort.assignSupplier(this.requestMapper.map(deliveryId,
                new DeliverySupplierRequestDto(supplierId.value(), vehicleId != null ? vehicleId.value() : null))));
    }

    @Override
    public DeliveryDetailsDto unassignSupplier(final DeliveryId deliveryId) {
        return map(this.deliveryPort.unassignSupplier(deliveryId));
    }

    @Override
    public DeliveryDetailsDto changeRoute(final DeliveryId deliveryId, final DeliveryRouteChangeDto route) {
        return map(this.deliveryPort.changeRoute(this.requestMapper.map(deliveryId, new DeliveryRouteRequestDto(
                value(route.originDepartmentId()), value(route.destinationDepartmentId()), route.pickupMethod(),
                route.pickupPointId(), route.deliveryPickupPointId()))));
    }

    @Override
    public DeliveryDetailsDto changeConditions(final DeliveryId deliveryId,
                                               final DeliveryConditionsChangeDto conditions) {
        if (conditions == null || conditions.method() == null) {
            throw new IllegalArgumentException("Delivery method is required");
        }
        return map(this.deliveryPort.changeConditions(this.requestMapper.map(deliveryId,
                new DeliveryConditionsRequestDto(conditions.method(), conditions.signatureRequired(),
                        conditions.instructions(), conditions.plannedAt()))));
    }

    @Override
    public DeliveryDetailsDto commentStep(final DeliveryId deliveryId, final int stepNumber, final String comment) {
        return map(this.deliveryPort.commentStep(this.requestMapper.map(deliveryId, stepNumber,
                new DeliveryStepCommentRequestDto(comment))));
    }

    @Override
    public DeliveryDetailsDto correctStepStatus(final DeliveryId deliveryId, final int stepNumber,
                                                final String deliveryStatus) {
        if (deliveryStatus == null) {
            throw new IllegalArgumentException("Delivery status is required");
        }
        return map(this.deliveryPort.correctStepStatus(this.requestMapper.map(deliveryId, stepNumber,
                new DeliveryStepStatusRequestDto(deliveryStatus))));
    }

    @Override
    public void cancelDelivery(final DeliveryId deliveryId) {
        this.deliveryPort.cancelDelivery(deliveryId);
    }

    private DeliveryDetailsDto map(final DeliverySnapshot delivery) {
        final DeliveryResponseDto response = this.responseMapper.map(delivery);
        return new DeliveryDetailsDto(
                new DeliveryId(response.id()),
                response.shipmentId() != null ? new ShipmentId(Long.valueOf(response.shipmentId())) : null,
                response.type(),
                response.deliveryStatus(),
                response.lifecycleStatus(),
                departmentId(response.originDepartmentId()),
                departmentId(response.destinationDepartmentId()),
                response.pickupMethod(),
                response.method(),
                response.pickupPointId(),
                response.deliveryPickupPointId(),
                supplierId(response.supplierId()),
                response.vehicleId() != null ? new VehicleId(Long.valueOf(response.vehicleId())) : null,
                response.signatureRequired(),
                response.instructions(),
                response.createdAt(),
                response.plannedAt(),
                response.deliveredAt(),
                response.steps() == null ? List.of() : response.steps().stream().map(this::map).toList());
    }

    private DeliveryStepDetailsDto map(final DeliveryStepResponseDto step) {
        return new DeliveryStepDetailsDto(step.stepNumber(), step.attemptedAt(), step.outcome(),
                step.deliveryStatus(), supplierId(step.supplierId()), step.comment(), step.failureReason());
    }

    private DepartmentId departmentId(final String value) {
        return value != null ? new DepartmentId(Long.valueOf(value)) : null;
    }

    private SupplierId supplierId(final String value) {
        return value != null ? new SupplierId(Long.valueOf(value)) : null;
    }

    private Long value(final DepartmentId departmentId) {
        return departmentId != null ? departmentId.getValue() : null;
    }
}
