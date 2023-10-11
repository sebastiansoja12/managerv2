package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeliveryTokenMockAdapter implements DeliveryTokenServicePort {

    private final String supplierTokenServiceApplicationId = "1";

    private final String tokenProtection = "abcdefghjklk";

	@Override
	public DeliveryTokenResponse protect(DeliveryTokenRequest request) {
		final List<DeliveryPackageResponse> deliveryPackageResponses = deliveryPackageRequests(
				request.getDeliveryPackageRequests());
		return new DeliveryTokenResponse(deliveryPackageResponses);
	}

	private List<DeliveryPackageResponse> deliveryPackageRequests(List<DeliveryPackageRequest> requests) {
		return requests.stream()
				.map(deliveryPackageRequest -> new DeliveryPackageResponse(deliveryPackageRequest.getParcel(),
						deliveryPackageRequest.getSupplier().getSupplierCode(), supplierTokenServiceApplicationId,
						createProtectedDelivery(deliveryPackageRequest.getDelivery().getId())))
				.toList();
	}

    private ProtectedDelivery createProtectedDelivery(String deliveryId) {
        return new ProtectedDelivery(tokenProtection, deliveryId);
    }
}
