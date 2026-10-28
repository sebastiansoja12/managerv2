package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.domain.enumeration.DeliverySaveStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryTargetType;
import com.warehouse.logistics.domain.enumeration.UpdateStatus;
import com.warehouse.logistics.domain.vo.DeliveryTarget;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliveryCompletionRequestDto;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliverySupplierAssignmentRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryTargetDto;

public class DeliveryServiceAdapter implements DeliveryServicePort {

    private final DeliveryApiService deliveryApiService;

    public DeliveryServiceAdapter(final DeliveryApiService deliveryApiService) {
        this.deliveryApiService = deliveryApiService;
    }

    @Override
    public LogisticsResponse recordAttempt(final LogisticsRequest request, final UserId userId,
                                           final DepartmentId departmentId) {
        final DeliveryAttemptRequestDto attemptRequest = new DeliveryAttemptRequestDto(
                new DeliveryTargetDto(DeliveryTargetDto.TargetType.valueOf(request.getTarget().type().name()),
                        request.getTarget().id()),
                request.getProcessType() == ProcessType.RETURN
                        ? DeliveryAttemptRequestDto.DeliveryType.RETURN
                        : DeliveryAttemptRequestDto.DeliveryType.OUTBOUND,
                request.getDeliveryStatus(),
                userId,
                request.getSupplierId(),
                departmentId,
                request.getVehicleId(),
                request.getRejectReason() == null ? null : request.getRejectReason().value(),
                request.getDeliveryToken() == null ? null : request.getDeliveryToken().getValue());
        final DeliveryAttemptResultDto attemptResult = this.deliveryApiService.recordAttempt(attemptRequest);
        return new LogisticsResponse(attemptResult.deliveryId(), UpdateStatus.OK, attemptResult.shipmentId(),
                DeliverySaveStatus.SAVED, new DeliveryTarget(
                        DeliveryTargetType.valueOf(attemptResult.target().type().name()), attemptResult.target().id()));
    }

    @Override
    public void completeShipmentDelivery(final ShipmentId shipmentId, final boolean recipientSigned,
                                         final String recipientName, final String signature) {
        this.deliveryApiService.completeShipmentDelivery(
                new ShipmentDeliveryCompletionRequestDto(shipmentId, recipientSigned, recipientName, signature));
    }

    @Override
    public void assignShipmentSupplier(final ShipmentId shipmentId, final SupplierId supplierId,
                                       final VehicleId vehicleId) {
        this.deliveryApiService.assignShipmentSupplier(
                new ShipmentDeliverySupplierAssignmentRequestDto(shipmentId, supplierId, vehicleId));
    }
}
