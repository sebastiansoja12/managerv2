package com.warehouse.delivery.domain.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;

import com.warehouse.delivery.domain.vo.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTokenServicePort deliveryTokenServicePort;

    @Override
    public List<Delivery> save(Set<DeliveryRequest> deliveryRequest) {
        final List<Delivery> deliveries = deliveryRequest.stream()
                .map(deliveryRepository::saveDelivery)
                .toList();

        final DeliveryTokenRequest deliveryTokenRequest = buildTokenRequest(deliveries);

        final DeliveryTokenResponse deliveryTokenResponse = secureDelivery(deliveryTokenRequest);

        final Map<UUID, SupplierSignature> signaturesMap = assignToHashMap(deliveryTokenResponse);

        assignTokenToDelivery(signaturesMap, deliveries);

		return deliveries;
    }

    private DeliveryTokenRequest buildTokenRequest(List<Delivery> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        final Supplier supplier = deliveries.stream()
                .map(Delivery::getSupplierCode)
                .map(Supplier::new)
                .findAny()
                .orElse(null);
        return DeliveryTokenRequest.builder()
                .deliveryPackageRequests(deliveryPackageRequests)
                .supplier(supplier)
                .build();
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(Delivery delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(Delivery delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(buildDeliveryInformation(delivery))
                .build();
    }

    private DeliveryInformation buildDeliveryInformation(Delivery delivery) {
        return DeliveryInformation.builder()
                .deliveryStatus(delivery.getDeliveryStatus())
                .id(delivery.getId())
                .token(delivery.getToken())
                .depotCode(delivery.getDepotCode())
                .parcelId(delivery.getParcelId())
                .build();
    }

    private void assignTokenToDelivery(Map<UUID, SupplierSignature> supplierTokenResponseMap,
			List<Delivery> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierSignature supplierSignature = supplierTokenResponseMap.get(delivery.getId());
			delivery.setToken(supplierSignature.getToken());
		});
	}

    private Map<UUID, SupplierSignature> assignToHashMap(DeliveryTokenResponse responses) {
        return responses.getSupplierSignature().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private UUID generateKeyFromResponse(SupplierSignature supplierSignature) {
		if (supplierSignature != null) {
			return supplierSignature.getDeliveryId();
		}
		return null;
	}

    private DeliveryTokenResponse secureDelivery(DeliveryTokenRequest request) {
        return deliveryTokenServicePort.protect(request);
    }
}
