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
        final DeliveryTokenRequest deliveryTokenRequest = buildTokenRequest(deliveryRequest);

        final DeliveryTokenResponse deliveryTokenResponse = secureDelivery(deliveryTokenRequest);

        final Map<Long, SupplierSignature> signaturesMap = assignToHashMap(deliveryTokenResponse);

        assignTokenToDelivery(signaturesMap, deliveryRequest);

		return deliveryRequest
                .stream()
                .map(deliveryRepository::saveDelivery)
                .toList();
    }

    private DeliveryTokenRequest buildTokenRequest(Set<DeliveryRequest> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        final Supplier supplier = deliveries.stream()
                .map(DeliveryRequest::getSupplierCode)
                .map(Supplier::new)
                .findAny()
                .orElse(null);
        return DeliveryTokenRequest.builder()
                .deliveryPackageRequests(deliveryPackageRequests)
                .supplier(supplier)
                .build();
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(DeliveryRequest delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(DeliveryRequest delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(buildDeliveryInformation(delivery))
                .build();
    }

    private DeliveryInformation buildDeliveryInformation(DeliveryRequest delivery) {
        return DeliveryInformation.builder()
                .deliveryStatus(delivery.getDeliveryStatus())
                .depotCode(delivery.getDepotCode())
                .parcelId(delivery.getParcelId())
                .build();
    }

    private void assignTokenToDelivery(Map<Long, SupplierSignature> supplierTokenResponseMap,
			Set<DeliveryRequest> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierSignature supplierSignature = supplierTokenResponseMap.get(delivery.getParcelId());
			delivery.assignTokenToDelivery(supplierSignature.getToken());
		});
	}

    private Map<Long, SupplierSignature> assignToHashMap(DeliveryTokenResponse responses) {
        return responses.getSupplierSignature().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private Long generateKeyFromResponse(SupplierSignature supplierSignature) {
		if (supplierSignature != null) {
			return supplierSignature.getParcelId();
		}
		return null;
	}

    private DeliveryTokenResponse secureDelivery(DeliveryTokenRequest request) {
        return deliveryTokenServicePort.protect(request);
    }
}
