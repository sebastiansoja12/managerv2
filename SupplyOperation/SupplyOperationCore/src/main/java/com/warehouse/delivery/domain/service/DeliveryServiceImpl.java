package com.warehouse.delivery.domain.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.delivery.domain.vo.*;

public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTokenServicePort deliveryTokenServicePort;

    public DeliveryServiceImpl(final DeliveryRepository deliveryRepository,
                               final DeliveryTokenServicePort deliveryTokenServicePort) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryTokenServicePort = deliveryTokenServicePort;
    }

    @Override
    public Set<DeliveryResponse> save(final Set<DeliveryRequest> deliveryRequest) {
        final DeliveryTokenRequest deliveryTokenRequest = buildTokenRequest(deliveryRequest);

        final DeliveryTokenResponse deliveryTokenResponse = secureDelivery(deliveryTokenRequest);

        final Map<ShipmentId, SupplierSignature> signaturesMap = assignToHashMap(deliveryTokenResponse);

        assignTokenToDelivery(signaturesMap, deliveryRequest);

		return Collections.emptySet();
    }

    private DeliveryTokenRequest buildTokenRequest(final Set<DeliveryRequest> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        return DeliveryTokenRequest.builder()
                .deliveryPackageRequests(deliveryPackageRequests)
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
                .build();
    }

    private void assignTokenToDelivery(final Map<ShipmentId, SupplierSignature> supplierTokenResponseMap,
			Set<DeliveryRequest> deliveries) {
		deliveries.forEach(delivery -> {
			final SupplierSignature supplierSignature = supplierTokenResponseMap.get(delivery.getShipmentId());

		});
	}

    private Map<ShipmentId, SupplierSignature> assignToHashMap(DeliveryTokenResponse responses) {
        return responses.getSupplierSignature().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private ShipmentId generateKeyFromResponse(final SupplierSignature supplierSignature) {
		if (supplierSignature != null) {
			return null;
		}
		return null;
	}

    private DeliveryTokenResponse secureDelivery(final DeliveryTokenRequest request) {
        return deliveryTokenServicePort.protect(request);
    }
}
