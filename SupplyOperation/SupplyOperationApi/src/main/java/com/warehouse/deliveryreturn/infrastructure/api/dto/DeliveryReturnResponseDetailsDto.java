package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryIdDto;
import com.warehouse.delivery.dto.DeliveryStatusDto;

public class DeliveryReturnResponseDetailsDto {
    private final ProcessIdDto processId;
    private final DeliveryIdDto deliveryId;
    private final DeliveryStatusDto deliveryStatus;
    private final ReturnTokenDto returnToken;

    public DeliveryReturnResponseDetailsDto(final ProcessIdDto processId,
                                            final DeliveryIdDto deliveryId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final ReturnTokenDto returnToken) {
        this.processId = processId;
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
    }

    public DeliveryIdDto getDeliveryId() {
        return deliveryId;
    }

    public ProcessIdDto getProcessId() {
        return processId;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public ReturnTokenDto getReturnToken() {
        return returnToken;
    }
}
