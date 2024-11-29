package com.warehouse.deliverymissed.domain.service;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.ParcelStatusServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.UpdateStatusParcelRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryMissedServiceImpl implements DeliveryMissedService {

    private final DeliveryMissedRepository deliveryMissedRepository;

    private final ParcelStatusServicePort parcelStatusServicePort;

    @Override
    public DeliveryMissed saveDelivery(DeliveryMissedRequest request) {
        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .build();
        parcelStatusServicePort.updateParcelStatus(updateStatusParcelRequest);
        return deliveryMissedRepository.saveDeliveryMissed(request);
    }
}
