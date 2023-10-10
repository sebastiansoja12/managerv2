package com.warehouse.delivery.domain.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;

import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

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

        final List<SupplierTokenResponse> supplierTokenResponses = secureDelivery(supplierTokenRequests);

        final Map<List<UUID>, SupplierTokenResponse> supplierTokenResponseMap = assignToHashMap(supplierTokenResponses);

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
        return new SupplierTokenRequest(createDeliveryPackageRequests(delivery));
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(Delivery delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(Delivery delivery) {
        return new DeliveryPackageRequest(
                delivery.getParcelId(), new Supplier(delivery.getSupplierCode()), delivery);
    }

    private void assignTokenToDelivery(Map<List<UUID>, SupplierTokenResponse> supplierTokenResponseMap,
			List<Delivery> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierTokenResponse supplierTokenResponse = supplierTokenResponseMap.get(delivery.getId());
            // TODO do poprawy
			delivery.setToken(supplierTokenResponse.getSupplierSignature().toString());
		});
	}

    private Map<List<UUID>, SupplierTokenResponse> assignToHashMap(List<SupplierTokenResponse> responses) {
        return responses.stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

	private List<UUID> generateKeyFromResponse(SupplierTokenResponse response) {
		if (response != null && !CollectionUtils.isEmpty(response.getSupplierSignature())) {
			return response.getSupplierSignature().stream()
                    .map(SupplierSignature::getDeliveryId)
                    .collect(Collectors.toList());
		}
		return null;
	}

    private List<SupplierTokenResponse> secureDelivery(List<SupplierTokenRequest> requests) {
        final List<SupplierTokenResponse> responses = new ArrayList<>();
        requests.forEach(request -> responses.add(supplierTokenServicePort.protect(request)));
        return responses;
    }
}
