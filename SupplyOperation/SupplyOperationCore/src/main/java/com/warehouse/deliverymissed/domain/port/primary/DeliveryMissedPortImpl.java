package com.warehouse.deliverymissed.domain.port.primary;


import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DeliveryMissedPortImpl implements DeliveryMissedPort {

    private final DeliveryMissedService deliveryMissedService;

    private final RouteLogMissedServicePort logMissedServicePort;

    @Override
    public DeliveryMissedResponse logMissedDelivery(DeliveryMissedRequest request) {
        final DeliveryMissed deliveryMissed = deliveryMissedService.saveDelivery(request);
        logMissedServicePort.logRouteLogMissedDelivery(deliveryMissed);
        return DeliveryMissedResponse
                .builder()
                .deliveryId(deliveryMissed.getDeliveryId())
                .depotCode(deliveryMissed.getDepotCode())
                .parcelId(deliveryMissed.getParcelId())
                .supplierCode(deliveryMissed.getSupplierCode())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
