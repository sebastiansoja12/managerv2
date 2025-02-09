package com.warehouse.logistics.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.domain.vo.*;

public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository logisticsRepository;

    private final DeliveryTokenServicePort deliveryTokenServicePort;

    public LogisticsServiceImpl(final LogisticsRepository logisticsRepository,
                                final DeliveryTokenServicePort deliveryTokenServicePort) {
        this.logisticsRepository = logisticsRepository;
        this.deliveryTokenServicePort = deliveryTokenServicePort;
    }

    @Override
    public Set<LogisticsResponse> save(final Set<LogisticsRequest> logisticsRequests) {
		return logisticsRequests.stream()
                .map(logisticsRepository::create)
                .collect(Collectors.toSet());
    }

    private DeliveryTokenRequest buildTokenRequest(final Set<LogisticsRequest> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        return DeliveryTokenRequest.builder()
                .deliveryPackageRequests(deliveryPackageRequests)
                .build();
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(LogisticsRequest delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(LogisticsRequest delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(buildDeliveryInformation(delivery))
                .build();
    }

    private DeliveryInformation buildDeliveryInformation(LogisticsRequest delivery) {
        return DeliveryInformation.builder()
                .build();
    }

    private void assignTokenToDelivery(final Map<ShipmentId, SupplierSignature> supplierTokenResponseMap,
			Set<LogisticsRequest> deliveries) {
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
