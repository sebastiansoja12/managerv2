package com.warehouse.deliveryreturn.domain.service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.deliveryreturn.domain.model.*;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnInformation;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryReturnServiceImpl implements DeliveryReturnService {

    private final DeliveryReturnRepository deliveryReturnRepository;

    private final DeliveryReturnTokenServicePort deliveryReturnTokenServicePort;

    @Override
    public List<DeliveryReturn> deliverReturn(Set<DeliveryReturnDetails> deliveryReturnRequests) {

        final List<DeliveryReturn> deliveries = deliveryReturnRequests.stream()
                .map(deliveryReturnRepository::saveDeliveryReturn)
                .toList();

        final DeliveryReturnTokenRequest deliveryReturnTokenRequest = buildTokenRequest(deliveries);

        final DeliveryReturnTokenResponse deliveryReturnTokenResponse = secureDeliveryReturn(deliveryReturnTokenRequest);

        final Map<UUID, DeliveryReturnSignature> signaturesMap = assignToHashMap(deliveryReturnTokenResponse);

        assignTokenToDeliveryReturn(signaturesMap, deliveries);

        return deliveries;
    }

    private DeliveryReturnTokenRequest buildTokenRequest(List<DeliveryReturn> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        final Supplier supplier = deliveries.stream()
                .map(DeliveryReturn::getSupplierCode)
                .map(Supplier::new)
                .findAny()
                .orElse(null);
        return DeliveryReturnTokenRequest.builder()
                .requests(deliveryPackageRequests)
                .supplier(supplier)
                .build();
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(DeliveryReturn delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(DeliveryReturn delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(buildDeliveryInformation(delivery))
                .build();
    }

    private DeliveryReturnInformation buildDeliveryInformation(DeliveryReturn delivery) {
        return DeliveryReturnInformation.builder()
                .deliveryStatus(delivery.getDeliveryStatus())
                .id(delivery.getId())
                .token(delivery.getToken())
                .depotCode(delivery.getDepotCode())
                .parcelId(delivery.getParcelId())
                .build();
    }

	private void assignTokenToDeliveryReturn(Map<UUID, DeliveryReturnSignature> deliveryReturnSignatureMap,
			List<DeliveryReturn> deliveries) {
		deliveries.stream().map(delivery -> {
			final DeliveryReturnSignature deliveryReturnSignature = deliveryReturnSignatureMap.get(delivery.getId());
            return DeliveryReturn.builder()
                    .supplierCode(delivery.getSupplierCode())
                    .deliveryStatus(delivery.getDeliveryStatus())
                    .id(delivery.getId())
                    .token(deliveryReturnSignature.getToken())
                    .depotCode(delivery.getDepotCode())
                    .parcelId(delivery.getParcelId())
                    .build();
		});
	}

    private Map<UUID, DeliveryReturnSignature> assignToHashMap(DeliveryReturnTokenResponse responses) {
        return responses.getDeliveryReturnSignatures().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private UUID generateKeyFromResponse(DeliveryReturnSignature deliveryReturnSignature) {
        if (deliveryReturnSignature != null) {
            return deliveryReturnSignature.getDeliveryId();
        }
        return null;
    }

    private DeliveryReturnTokenResponse secureDeliveryReturn(DeliveryReturnTokenRequest request) {
        return deliveryReturnTokenServicePort.sign(request);
    }
}
