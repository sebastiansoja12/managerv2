package com.warehouse.deliverymissed.domain.port.primary;


import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.deliverymissed.domain.exception.EmptyDepotCodeException;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryMissedPortImpl implements DeliveryMissedPort {

    private final DeliveryMissedService deliveryMissedService;

    private final RouteLogMissedServicePort logMissedServicePort;

    @Override
    public DeliveryMissedResponse logMissedDelivery(DeliveryMissedRequest request) {
        validateDeliveryMissedRequest(request);
        final DeliveryMissed deliveryMissed = deliveryMissedService.saveDelivery(request);
        logMissedDelivery(deliveryMissed);
        logDepotCode(deliveryMissed);
        logSupplierCode(deliveryMissed);
        return DeliveryMissedResponse
                .builder()
                .deliveryId(deliveryMissed.getDeliveryId())
                .depotCode(deliveryMissed.getDepotCode())
                .parcelId(deliveryMissed.getParcelId())
                .supplierCode(deliveryMissed.getSupplierCode())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void logMissedDelivery(DeliveryMissed deliveryMissed) {
        logMissedServicePort.logRouteLogMissedDelivery(deliveryMissed);
    }

    private void logDepotCode(DeliveryMissed deliveryMissed) {
        logMissedServicePort.logDepotCodeMissedDelivery(deliveryMissed);
    }

    private void logSupplierCode(DeliveryMissed deliveryMissed) {
        logMissedServicePort.logSupplierCode(deliveryMissed);
    }

    private void validateDeliveryMissedRequest(DeliveryMissedRequest request) {
        if (StringUtils.isEmpty(request.getDepotCode())) {
            throw new EmptyDepotCodeException("Depot code cannot be empty");
        }
    }
}
