package com.warehouse.suppliertoken.infrastructure.adapter.secondary;

import com.warehouse.suppliertoken.domain.model.*;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SupplierTokenMockAdapter implements SupplierTokenServicePort {

    private final String supplierTokenServiceApplicationId = "1";

    private final String tokenProtection = "abcdefghjklk";

	@Override
	public SupplierTokenResponse protect(SupplierTokenRequest request) {
		final List<DeliveryPackageResponse> deliveryPackageResponses = deliveryPackageRequests(
				request.getDeliveryPackageRequests());
		return new SupplierTokenResponse(deliveryPackageResponses);
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
