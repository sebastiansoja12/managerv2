package com.warehouse.delivery.domain.port.primary;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.delivery.domain.port.secondary.RouteLogDeliveryStatusServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryPortImpl implements DeliveryPort {

    private final DeliveryService deliveryService;

    private final RouteLogDeliveryStatusServicePort logServicePort;
    
    private final ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Override
    public List<DeliveryResponse> deliver(List<DeliveryRequest> deliveryRequest) {
        final Set<DeliveryRequest> deliveryRequests = deliveryRequest.stream()
                .filter(Objects::nonNull)
                .peek(DeliveryRequest::updateDeliveryStatus)
                .collect(Collectors.toSet());

        final List<Delivery> signedDeliveries = deliveryService.save(deliveryRequests);
        
        updateParcelStatus(signedDeliveries);

        registerDeliveryRoute(signedDeliveries);
        
        return signedDeliveries
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void registerDeliveryRoute(List<Delivery> signedDeliveries) {
        logServicePort.deliver(new HashSet<>(signedDeliveries));
    }
    
    private void updateParcelStatus(List<Delivery> signedDeliveries) {
        signedDeliveries.forEach(this::updateParcelStatus);
    }
    
    private void updateParcelStatus(Delivery delivery) {
		final UpdateStatus updateStatus = parcelStatusControlChangeServicePort
				.updateParcelStatus(new UpdateStatusParcelRequest(delivery.getParcelId()));

        delivery.updateStatus(updateStatus);

    }

    private DeliveryResponse mapToResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .parcelId(delivery.getParcelId())
                .deliveryStatus(delivery.getDeliveryStatus().name())
                .build();
    }
}
