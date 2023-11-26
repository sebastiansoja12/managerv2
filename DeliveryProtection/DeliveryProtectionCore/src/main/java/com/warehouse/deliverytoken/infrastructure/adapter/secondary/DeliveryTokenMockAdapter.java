package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.domain.vo.DeliveryPackageRequest;
import com.warehouse.deliverytoken.domain.vo.DeliveryPackageResponse;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.vo.ProtectedDelivery;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.model.SupplierToken;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.SupplierNotAllowedException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class DeliveryTokenMockAdapter implements DeliveryTokenServicePort {

    private final String supplierTokenServiceApplicationId = "1";

    private final String tokenProtection = "abcdefghjklk";

	private final Map<String, SupplierToken> supplierTokenMap;

	@Override
	public DeliveryTokenResponse protect(DeliveryTokenRequest request) {
		validateSupplier(request);
		final List<DeliveryPackageResponse> deliveryPackageResponses = deliveryPackageRequests(
				request.getDeliveryPackageRequests());
		return new DeliveryTokenResponse(deliveryPackageResponses, request.extractSupplierCode());
	}

	private void validateSupplier(DeliveryTokenRequest request) {
		final String supplierCode = request.extractSupplierCode();
		final SupplierToken token = supplierTokenMap.get(supplierCode);
		if (Objects.isNull(token)) {
			throw new SupplierNotAllowedException(supplierCode);
		}
	}

	private List<DeliveryPackageResponse> deliveryPackageRequests(List<DeliveryPackageRequest> requests) {
		return requests.stream()
				.map(deliveryPackageRequest -> new DeliveryPackageResponse(
						deliveryPackageRequest.getParcel(), supplierTokenServiceApplicationId,
						createProtectedDelivery(deliveryPackageRequest.getDelivery().getId())))
				.toList();
	}

    private ProtectedDelivery createProtectedDelivery(String deliveryId) {
        return new ProtectedDelivery(tokenProtection, deliveryId);
    }
}
