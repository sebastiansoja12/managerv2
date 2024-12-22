package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;
import com.warehouse.deliveryreturn.domain.vo.ReturnPackageRequest;
import com.warehouse.deliveryreturn.domain.vo.ReturnToken;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryReturnServiceMockAdapter implements DeliveryReturnTokenServicePort {

    private final static String TOKEN = "12345";

    @Override
    public ReturnTokenResponse sign(final ReturnTokenRequest returnTokenRequest) {
		return new ReturnTokenResponse(map(returnTokenRequest.getReturnPackageRequests()),
                returnTokenRequest.getSupplier().getSupplierCode());
	}
    
    private List<DeliveryReturnSignature> map(final List<ReturnPackageRequest> packageRequests) {
        return packageRequests.stream()
                .map(packageRequest -> DeliveryReturnSignature.builder()
                        .shipmentId(packageRequest.getShipmentId())
                        .returnToken(new ReturnToken(TOKEN))
                        .build())
                .collect(Collectors.toList());
    }
}
