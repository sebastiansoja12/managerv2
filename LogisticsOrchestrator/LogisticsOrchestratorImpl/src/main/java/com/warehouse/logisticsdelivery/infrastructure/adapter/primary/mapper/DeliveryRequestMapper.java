package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.application.port.primary.command.AssignDeliverySupplierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.AttachDeliveryStepSignatureCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryConditionsCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryRouteCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CommentDeliveryStepCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CorrectDeliveryStepStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ReassignDeliveryStepCarrierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RegisterDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryAttemptRegisterRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryConditionsRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryRouteRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepCarrierRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepCommentRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepSignatureRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliveryStepStatusRequestDto;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DeliverySupplierRequestDto;

import java.util.UUID;

public class DeliveryRequestMapper {

    public AssignDeliverySupplierCommand map(final DeliveryId deliveryId, final DeliverySupplierRequestDto request) {
        return new AssignDeliverySupplierCommand(deliveryId, supplierId(request.supplierId()),
                vehicleId(request.vehicleId()));
    }

    public ChangeDeliveryRouteCommand map(final DeliveryId deliveryId, final DeliveryRouteRequestDto request) {
        return new ChangeDeliveryRouteCommand(deliveryId, departmentId(request.originDepartmentId()),
                departmentId(request.destinationDepartmentId()),
                request.pickupMethod() == null ? null : PickupMethod.valueOf(request.pickupMethod()),
                pickupPointId(request.pickupPointId()), pickupPointId(request.deliveryPickupPointId()));
    }

    public ChangeDeliveryConditionsCommand map(final DeliveryId deliveryId,
                                               final DeliveryConditionsRequestDto request) {
        return new ChangeDeliveryConditionsCommand(deliveryId,
                request.method() == null ? null : DeliveryMethod.valueOf(request.method()),
                request.signatureRequired(), request.instructions(), request.plannedAt());
    }

    public RegisterDeliveryAttemptCommand map(final DeliveryId deliveryId,
                                              final DeliveryAttemptRegisterRequestDto request) {
        return new RegisterDeliveryAttemptCommand(deliveryId, deliveryStatus(request.deliveryStatus()),
                request.comment());
    }

    public CommentDeliveryStepCommand map(final DeliveryId deliveryId, final int stepNumber,
                                          final DeliveryStepCommentRequestDto request) {
        return new CommentDeliveryStepCommand(deliveryId, stepNumber, request.comment());
    }

    public CorrectDeliveryStepStatusCommand map(final DeliveryId deliveryId, final int stepNumber,
                                                final DeliveryStepStatusRequestDto request) {
        return new CorrectDeliveryStepStatusCommand(deliveryId, stepNumber, deliveryStatus(request.deliveryStatus()));
    }

    public AttachDeliveryStepSignatureCommand map(final DeliveryId deliveryId, final int stepNumber,
                                                  final DeliveryStepSignatureRequestDto request) {
        return new AttachDeliveryStepSignatureCommand(deliveryId, stepNumber,
                request.signatureId() == null ? null : new SignatureId(request.signatureId()));
    }

    public ReassignDeliveryStepCarrierCommand map(final DeliveryId deliveryId, final int stepNumber,
                                                  final DeliveryStepCarrierRequestDto request) {
        return new ReassignDeliveryStepCarrierCommand(deliveryId, stepNumber, supplierId(request.supplierId()),
                vehicleId(request.vehicleId()));
    }

    private DeliveryStatus deliveryStatus(final String value) {
        return value == null ? null : DeliveryStatus.valueOf(value);
    }

    private SupplierId supplierId(final Long value) {
        return value == null ? null : new SupplierId(value);
    }

    private VehicleId vehicleId(final Long value) {
        return value == null ? null : new VehicleId(value);
    }

    private DepartmentId departmentId(final Long value) {
        return value == null ? null : new DepartmentId(value);
    }

    private PickupPointId pickupPointId(final String value) {
        return value == null || value.isBlank() ? null : new PickupPointId(UUID.fromString(value));
    }
}
