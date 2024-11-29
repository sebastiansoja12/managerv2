package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;

public class DeliveryReturnResponseDetailsDto {
    private ProcessIdDto processId;
    private DeliveryStatusDto deliveryStatus;
    private ReturnTokenDto returnToken;
    private UpdateStatusDto updateStatus;

    public DeliveryReturnResponseDetailsDto() {
    }

    public DeliveryReturnResponseDetailsDto(final ProcessIdDto processId,
                                            final DeliveryStatusDto deliveryStatus,
                                            final ReturnTokenDto returnToken,
                                            final UpdateStatusDto updateStatus) {
        this.processId = processId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
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

    public UpdateStatusDto getUpdateStatus() {
        return updateStatus;
    }
}
