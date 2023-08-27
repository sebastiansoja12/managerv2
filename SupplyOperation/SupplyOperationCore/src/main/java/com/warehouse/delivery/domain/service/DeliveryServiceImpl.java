package com.warehouse.delivery.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final SupplierTokenServicePort supplierTokenServicePort;

    @Override
    public List<Delivery> save(List<Delivery> deliveryRequest) {
        final List<Delivery> deliveries = deliveryRequest.stream()
                .map(deliveryRepository::saveDelivery)
                .toList();

        final List<SupplierTokenRequest> supplierTokenRequests = buildTokenRequests(deliveries);

        final List<SupplierTokenResponse> supplierTokenResponses = signSupplier(supplierTokenRequests);

        final Map<UUID, SupplierTokenResponse> supplierTokenResponseMap = assignToHashMap(supplierTokenResponses);

        assignTokenToDelivery(supplierTokenResponseMap, deliveries);

		return deliveries;
    }

    private List<SupplierTokenRequest> buildTokenRequests(List<Delivery> deliveries) {
        return deliveries
                .stream()
                .map(this::obtainRequest)
                .collect(Collectors.toList());
    }

    private SupplierTokenRequest obtainRequest(Delivery delivery) {
        return new SupplierTokenRequest(delivery.getSupplierCode(), delivery.getId());
    }

	private void assignTokenToDelivery(Map<UUID, SupplierTokenResponse> supplierTokenResponseMap, List<Delivery> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierTokenResponse supplierTokenResponse = supplierTokenResponseMap.get(delivery.getId());
			delivery.setToken(supplierTokenResponse.getSupplierSignature().getToken());
		});
    }

    private Map<UUID, SupplierTokenResponse> assignToHashMap(List<SupplierTokenResponse> responses) {
        return responses.stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

	private UUID generateKeyFromResponse(SupplierTokenResponse response) {
		if (response != null && response.getSupplierSignature() != null) {
			return response.getSupplierSignature().getId();
		}
		return null;
	}

    private List<SupplierTokenResponse> signSupplier(List<SupplierTokenRequest> requests) {
        final List<SupplierTokenResponse> responses = new ArrayList<>();
        requests.forEach(request -> responses.add(supplierTokenServicePort.sign(request)));
        return responses;
    }
}
