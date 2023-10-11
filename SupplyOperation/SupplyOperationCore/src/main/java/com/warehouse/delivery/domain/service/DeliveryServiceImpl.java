package com.warehouse.delivery.domain.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;

import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import static com.google.common.collect.MoreCollectors.onlyElement;

@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTokenServicePort deliveryTokenServicePort;

    @Override
    public List<Delivery> save(List<Delivery> deliveryRequest) {
        final List<Delivery> deliveries = deliveryRequest.stream()
                .map(deliveryRepository::saveDelivery)
                .toList();

        final List<DeliveryTokenRequest> deliveryTokenRequests = buildTokenRequests(deliveries);

        final List<SupplierTokenResponse> supplierTokenResponses = secureDelivery(deliveryTokenRequests);

        final Map<UUID, SupplierTokenResponse> supplierTokenResponseMap = assignToHashMap(supplierTokenResponses);

        assignTokenToDelivery(supplierTokenResponseMap, deliveries);

		return deliveries;
    }

    private List<DeliveryTokenRequest> buildTokenRequests(List<Delivery> deliveries) {
        return deliveries
                .stream()
                .map(this::obtainRequest)
                .collect(Collectors.toList());
    }

    private DeliveryTokenRequest obtainRequest(Delivery delivery) {
        return new DeliveryTokenRequest(createDeliveryPackageRequests(delivery));
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(Delivery delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(Delivery delivery) {
        return new DeliveryPackageRequest(
                delivery.getParcelId(), new Supplier(delivery.getSupplierCode()), delivery);
    }

    private void assignTokenToDelivery(Map<UUID, SupplierTokenResponse> supplierTokenResponseMap,
			List<Delivery> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierTokenResponse supplierTokenResponse = supplierTokenResponseMap.get(delivery.getId());
			supplierTokenResponse.getSupplierSignature()
                    .forEach(tokenSignature -> delivery.setToken(tokenSignature.getToken()));
		});
	}

    private Map<UUID, SupplierTokenResponse> assignToHashMap(List<SupplierTokenResponse> responses) {
        return responses.stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

	private UUID generateKeyFromResponse(SupplierTokenResponse response) {
		if (response != null && !CollectionUtils.isEmpty(response.getSupplierSignature())) {
			return response.getSupplierSignature().stream()
                    .map(SupplierSignature::getDeliveryId)
                    .map(UUID::toString)
                    .map(UUID::fromString)
                    .collect(onlyElement());
		}
		return null;
	}

    private List<SupplierTokenResponse> secureDelivery(List<DeliveryTokenRequest> requests) {
        final List<SupplierTokenResponse> responses = new ArrayList<>();
        requests.forEach(request -> responses.add(deliveryTokenServicePort.protect(request)));
        return responses;
    }
}
