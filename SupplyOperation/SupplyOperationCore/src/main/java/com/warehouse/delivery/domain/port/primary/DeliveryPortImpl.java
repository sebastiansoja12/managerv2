package com.warehouse.delivery.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;

import com.warehouse.delivery.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryPortImpl implements DeliveryPort {

    private final DeliveryService deliveryService;

    private final RouteLogServicePort logServicePort;
    
    private final ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Override
    public List<DeliveryResponse> deliver(List<DeliveryRequest> deliveryRequest) {
        final Set<DeliveryRequest> deliveryRequests = deliveryRequest.stream()
                .filter(Objects::nonNull)
                .peek(DeliveryRequest::updateDeliveryStatus)
                .collect(Collectors.toSet());

        final List<Delivery> signedDeliveries = deliveryService.save(deliveryRequests);

        registerDeliveryRoute(signedDeliveries);

        final List<DeliveryResponse> deliveryResponses = signedDeliveries.stream()
                .map(this::mapToResponse)
                .toList();
        
        updateParcelStatus(deliveryResponses);
        
        return deliveryResponses;
    }

    private void registerDeliveryRoute(List<Delivery> signedDeliveries) {
        final Set<DeliveryRouteRequest> deliveryRouteRequests = signedDeliveries.stream()
                .map(this::mapToDeliveryRouteRequest)
                .collect(Collectors.toSet());

        logServicePort.deliver(deliveryRouteRequests);
    }
    
    private void updateParcelStatus(List<DeliveryResponse> deliveryResponses) {
        deliveryResponses.forEach(this::updateParcelStatus);
    }
    
    private void updateParcelStatus(DeliveryResponse deliveryResponse) {
		final UpdateStatus updateStatus = parcelStatusControlChangeServicePort
				.updateParcelStatus(new UpdateStatusParcelRequest(deliveryResponse.getParcelId()));
        
        deliveryResponse.updateStatus(updateStatus);

    }

    private DeliveryRouteRequest mapToDeliveryRouteRequest(Delivery delivery) {
        return DeliveryRouteRequest.builder()
                .id(delivery.getId())
                .parcelId(delivery.getParcelId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .depotCode(delivery.getDepotCode())
                .supplierCode(delivery.getSupplierCode())
                .token(delivery.getToken())
                .build();
    }

    private DeliveryResponse mapToResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .parcelId(delivery.getParcelId())
                .deliveryStatus(delivery.getDeliveryStatus().name())
                .build();
    }
}
