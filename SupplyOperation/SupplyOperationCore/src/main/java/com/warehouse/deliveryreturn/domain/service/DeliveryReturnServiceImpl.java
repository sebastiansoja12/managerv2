package com.warehouse.deliveryreturn.domain.service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.deliveryreturn.domain.exception.SupplierNotAvailableInRequestException;
import com.warehouse.deliveryreturn.domain.model.*;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.*;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public class DeliveryReturnServiceImpl implements DeliveryReturnService {

    private final DeliveryReturnRepository deliveryReturnRepository;

    private final DeliveryReturnTokenServicePort deliveryReturnTokenServicePort;

    private final ParcelRepositoryServicePort parcelRepositoryServicePort;

    private final MailServicePort mailServicePort;

    @Override
    public List<DeliveryReturn> deliverReturn(Set<DeliveryReturnDetails> deliveryReturnRequests) {

        final DeliveryReturnTokenRequest deliveryReturnTokenRequest = buildTokenRequest(deliveryReturnRequests);

        final DeliveryReturnTokenResponse deliveryReturnTokenResponse = secureDeliveryReturn(deliveryReturnTokenRequest);

        final Map<Long, DeliveryReturnSignature> signaturesMap = assignToHashMap(deliveryReturnTokenResponse);

        final List<DeliveryReturnDetails> deliveries = assignTokenToDeliveryReturn(signaturesMap, 
                deliveryReturnRequests);

        return deliveries.stream()
                .peek(deliveryReturn -> {
                    final Parcel parcel = parcelRepositoryServicePort.downloadParcel(deliveryReturn.getParcelId());
                    mailServicePort.sendNotification(parcel);
                })
                .map(deliveryReturnRepository::saveDeliveryReturn)
                .toList();
    }

    private DeliveryReturnTokenRequest buildTokenRequest(Set<DeliveryReturnDetails> deliveries) {
        final List<DeliveryPackageRequest> deliveryPackageRequests = deliveries
                .stream()
                .map(this::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        final String supplierCode = deliveries.stream()
                .map(DeliveryReturnDetails::getSupplierCode)
                .filter(StringUtils::isNotEmpty)
                .findAny()
                .orElseThrow(() -> new SupplierNotAvailableInRequestException("Supplier not available"));
        return DeliveryReturnTokenRequest.builder()
                .requests(deliveryPackageRequests)
                .supplier(new Supplier(supplierCode, Boolean.TRUE))
                .build();
    }

    private List<DeliveryPackageRequest> createDeliveryPackageRequests(DeliveryReturnDetails delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(DeliveryReturnDetails delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(buildDeliveryInformation(delivery))
                .build();
    }

    private DeliveryReturnInformation buildDeliveryInformation(DeliveryReturnDetails delivery) {
        return DeliveryReturnInformation.builder()
                .deliveryStatus(String.valueOf(delivery.getDeliveryStatus()))
                .depotCode(delivery.getDepotCode())
                .parcelId(delivery.getParcelId())
                .locked(parcelRepositoryServicePort.downloadParcel(delivery.getParcelId()).getLocked())
                .build();
    }

	private List<DeliveryReturnDetails> assignTokenToDeliveryReturn(Map<Long, DeliveryReturnSignature> signaturesMap,
			Set<DeliveryReturnDetails> deliveryReturnRequests) {
        return deliveryReturnRequests.stream().map(delivery -> {
			final DeliveryReturnSignature deliveryReturnSignature = signaturesMap.get(delivery.getParcelId());
            return DeliveryReturnDetails.builder()
                    .supplierCode(delivery.getSupplierCode())
                    .deliveryStatus(delivery.getDeliveryStatus())
                    .token(deliveryReturnSignature != null ? deliveryReturnSignature.getToken() : null)
                    .depotCode(delivery.getDepotCode())
                    .parcelId(delivery.getParcelId())
                    .build();
		}).toList();
	}

    private Map<Long, DeliveryReturnSignature> assignToHashMap(DeliveryReturnTokenResponse responses) {
        return responses.getDeliveryReturnSignatures().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private Long generateKeyFromResponse(DeliveryReturnSignature deliveryReturnSignature) {
        if (deliveryReturnSignature != null) {
            return deliveryReturnSignature.getParcelId();
        }
        return null;
    }

    private DeliveryReturnTokenResponse secureDeliveryReturn(DeliveryReturnTokenRequest request) {
        return deliveryReturnTokenServicePort.sign(request);
    }
}
