package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryTargetDto;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

public class DeliveryApiMapper {

    public RecordDeliveryAttemptCommand map(final DeliveryAttemptRequestDto attemptRequest) {
        return new RecordDeliveryAttemptCommand(
                new DeliveryTarget(DeliveryTargetType.valueOf(attemptRequest.target().type().name()),
                        attemptRequest.target().id()),
                DeliveryType.valueOf(attemptRequest.deliveryType().name()),
                attemptRequest.deliveryStatus(),
                attemptRequest.userId(),
                attemptRequest.supplierId(),
                attemptRequest.departmentId(),
                attemptRequest.vehicleId(),
                attemptRequest.comment(),
                attemptRequest.token());
    }

    public DeliveryAttemptResultDto map(final DeliverySnapshot delivery) {
        return new DeliveryAttemptResultDto(
                delivery.deliveryId(),
                delivery.shipmentId(),
                new DeliveryTargetDto(DeliveryTargetDto.TargetType.valueOf(delivery.target().type().name()),
                        delivery.target().id()));
    }
}
