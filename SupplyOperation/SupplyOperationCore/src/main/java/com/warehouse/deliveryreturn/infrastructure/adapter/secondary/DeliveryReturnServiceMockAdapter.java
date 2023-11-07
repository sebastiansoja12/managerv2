package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryPackageRequest;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenResponse;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DeliveryReturnServiceMockAdapter implements DeliveryReturnTokenServicePort {

    private final static String TOKEN = "12345";

    @Override
    public DeliveryReturnTokenResponse sign(DeliveryReturnTokenRequest deliveryReturnTokenRequest) {
		return new DeliveryReturnTokenResponse(map(deliveryReturnTokenRequest.getRequests()),
				deliveryReturnTokenRequest.getSupplier().getSupplierCode());
	}
    
    private List<DeliveryReturnSignature> map(List<DeliveryPackageRequest> packageRequests) {
        return packageRequests.stream()
                .map(packageRequest -> DeliveryReturnSignature.builder()
                        .parcelId(packageRequest.getDelivery().getParcelId())
                        .token(TOKEN)
                        .build())
                .collect(Collectors.toList());
    }
}
