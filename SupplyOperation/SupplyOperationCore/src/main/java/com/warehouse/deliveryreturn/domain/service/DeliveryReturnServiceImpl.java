package com.warehouse.deliveryreturn.domain.service;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.deliveryreturn.domain.exception.SupplierNotAvailableInRequestException;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.*;


public class DeliveryReturnServiceImpl implements DeliveryReturnService {

    private final DeliveryReturnRepository deliveryReturnRepository;

    private final DeliveryReturnTokenServicePort deliveryReturnTokenServicePort;

    private final ShipmentRepositoryServicePort shipmentRepositoryServicePort;

    private final MailServicePort mailServicePort;

    public DeliveryReturnServiceImpl(final DeliveryReturnRepository deliveryReturnRepository,
                                     final DeliveryReturnTokenServicePort deliveryReturnTokenServicePort,
                                     final ShipmentRepositoryServicePort shipmentRepositoryServicePort,
                                     final MailServicePort mailServicePort) {
        this.deliveryReturnRepository = deliveryReturnRepository;
        this.deliveryReturnTokenServicePort = deliveryReturnTokenServicePort;
        this.shipmentRepositoryServicePort = shipmentRepositoryServicePort;
        this.mailServicePort = mailServicePort;
    }

    @Override
    public List<DeliveryReturn> deliverReturn(final Set<DeliveryReturnDetails> deliveryReturnRequests) {

        final DeliveryReturnTokenRequest deliveryReturnTokenRequest = buildTokenRequest(deliveryReturnRequests);

        final DeliveryReturnTokenResponse deliveryReturnTokenResponse = secureDeliveryReturn(deliveryReturnTokenRequest);

        final Map<Long, DeliveryReturnSignature> signaturesMap = assignToHashMap(deliveryReturnTokenResponse);

        final List<DeliveryReturnDetails> deliveries = assignTokenToDeliveryReturn(signaturesMap, 
                deliveryReturnRequests);

        return deliveries.stream()
                .peek(deliveryReturn -> {
                    final Shipment shipment = shipmentRepositoryServicePort.downloadShipment(deliveryReturn.getShipmentId());
                    mailServicePort.sendNotification(shipment);
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
                .filter(ObjectUtils::isNotEmpty)
                .findAny()
                .orElseThrow(() -> new SupplierNotAvailableInRequestException("Supplier not available"))
                .getValue();
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
                .depotCode(delivery.getDepartmentCode().getValue())
                .parcelId(delivery.getShipmentId().getValue())
                .locked(shipmentRepositoryServicePort.downloadShipment(delivery.getShipmentId()).isLocked())
                .build();
    }

	private List<DeliveryReturnDetails> assignTokenToDeliveryReturn(Map<Long, DeliveryReturnSignature> signaturesMap,
			Set<DeliveryReturnDetails> deliveryReturnRequests) {
        return deliveryReturnRequests.stream().map(delivery -> {
			final DeliveryReturnSignature deliveryReturnSignature = signaturesMap.get(delivery.getShipmentId());
            return DeliveryReturnDetails.builder()
                    .supplierCode(delivery.getSupplierCode())
                    .deliveryStatus(delivery.getDeliveryStatus())
                    .token(deliveryReturnSignature != null ? deliveryReturnSignature.getToken() : null)
                    .departmentCode(delivery.getDepartmentCode())
                    .shipmentId(delivery.getShipmentId())
                    .build();
		}).toList();
	}

    private Map<Long, DeliveryReturnSignature> assignToHashMap(DeliveryReturnTokenResponse responses) {
        return responses.getDeliveryReturnSignatures().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private Long generateKeyFromResponse(DeliveryReturnSignature deliveryReturnSignature) {
        return !Objects.isNull(deliveryReturnSignature) ? deliveryReturnSignature.getParcelId() : null;
    }

    private DeliveryReturnTokenResponse secureDeliveryReturn(DeliveryReturnTokenRequest request) {
        return deliveryReturnTokenServicePort.sign(request);
    }
}
