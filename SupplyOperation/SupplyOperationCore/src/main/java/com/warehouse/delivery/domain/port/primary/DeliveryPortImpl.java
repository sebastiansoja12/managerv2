package com.warehouse.delivery.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryPortImpl implements DeliveryPort {

    private final DeliveryService deliveryService;

    private final RouteLogServicePort logServicePort;

    @Override
    public List<DeliveryResponse> deliver(List<DeliveryRequest> deliveryRequest) {
        final Set<DeliveryRequest> deliveryRequests = deliveryRequest.stream()
                .filter(Objects::nonNull)
                .map(this::determineDeliveryStatus)
                .collect(Collectors.toSet());

        final List<Delivery> deliveries = deliveryRequests.stream()
                .map(this::mapToDelivery)
                .toList();

        final List<Delivery> signedDeliveries = deliveryService.save(deliveries);

        registerDeliveryRoute(signedDeliveries);

        return signedDeliveries.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void registerDeliveryRoute(List<Delivery> signedDeliveries) {
        final Set<DeliveryRouteRequest> deliveryRouteRequests = signedDeliveries.stream()
                .map(this::mapToDeliveryRouteRequest)
                .collect(Collectors.toSet());

        logServicePort.deliver(deliveryRouteRequests);
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


    private DeliveryRequest determineDeliveryStatus(DeliveryRequest deliveryRequest) {
        deliveryRequest.setDeliveryStatus(DeliveryStatus.DELIVERY);
        return deliveryRequest;
    }

    private DeliveryResponse mapToResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .parcelId(delivery.getParcelId())
                .deliveryStatus(delivery.getDeliveryStatus().name())
                .build();
    }

    private Delivery mapToDelivery(DeliveryRequest request) {
        return Delivery.builder()
                .deliveryStatus(request.getDeliveryStatus())
                .supplierCode(request.getSupplierCode())
                .depotCode(request.getDepotCode())
                .parcelId(request.getParcelId())
                .build();
    }
}
