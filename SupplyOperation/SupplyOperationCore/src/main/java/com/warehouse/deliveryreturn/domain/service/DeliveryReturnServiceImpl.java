package com.warehouse.deliveryreturn.domain.service;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.ReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.*;


public class DeliveryReturnServiceImpl implements DeliveryReturnService {

    private final DeliveryReturnRepository deliveryReturnRepository;

    private final ReturnTokenServicePort returnTokenServicePort;

    private final ShipmentRepositoryServicePort shipmentRepositoryServicePort;

    private final MailServicePort mailServicePort;

    public DeliveryReturnServiceImpl(final DeliveryReturnRepository deliveryReturnRepository,
                                     final ReturnTokenServicePort returnTokenServicePort,
                                     final ShipmentRepositoryServicePort shipmentRepositoryServicePort,
                                     final MailServicePort mailServicePort) {
        this.deliveryReturnRepository = deliveryReturnRepository;
        this.returnTokenServicePort = returnTokenServicePort;
        this.shipmentRepositoryServicePort = shipmentRepositoryServicePort;
        this.mailServicePort = mailServicePort;
    }

    @Override
    public List<DeliveryReturn> deliverReturn(final Set<DeliveryReturnDetails> deliveryReturnRequests,
                                              final DeviceInformation deviceInformation) {

        final ReturnTokenRequest returnTokenRequest = ReturnTokenRequest.from(deliveryReturnRequests, deviceInformation);

        final ReturnTokenResponse returnTokenResponse = returnTokenServicePort.sign(returnTokenRequest);

        final Map<ShipmentId, ReturnPackageResponse> signaturesMap = assignToHashMap(returnTokenResponse);

        final List<ReturnTokenDetails> deliveries = assignTokenToDeliveryReturn(signaturesMap,
                deliveryReturnRequests);

        return deliveries.stream()
                .peek(deliveryReturn -> {
                    final Shipment shipment = shipmentRepositoryServicePort.downloadShipment(deliveryReturn.getShipmentId());
                    mailServicePort.sendNotification(shipment);
                })
                .map(deliveryReturnDetails -> DeliveryReturn
                        .builder()
                        .token(deliveryReturnDetails.getReturnToken().value())
                        .supplierCode(deliveryReturnDetails.getSupplierCode().getValue())
                        .departmentCode(deliveryReturnDetails.getDepartmentCode().getValue())
                        .shipmentId(deliveryReturnDetails.getShipmentId().getValue())
                        .deliveryStatus(deliveryReturnDetails.getDeliveryStatus().name())
                        .build())
                .toList();
    }

	private List<ReturnTokenDetails> assignTokenToDeliveryReturn(final Map<ShipmentId, ReturnPackageResponse> signaturesMap,
			final Set<DeliveryReturnDetails> deliveryReturnRequests) {
        return deliveryReturnRequests.stream().map(delivery -> {
			final ReturnPackageResponse returnPackageResponse = signaturesMap.get(delivery.getShipmentId());
            return ReturnTokenDetails.builder()
                    .supplierCode(delivery.getSupplierCode())
                    .deliveryStatus(delivery.getDeliveryStatus())
                    .departmentCode(delivery.getDepartmentCode())
                    .shipmentId(delivery.getShipmentId())
                    .returnToken(returnPackageResponse.getReturnToken())
                    .build();
		}).toList();
	}

    private Map<ShipmentId, ReturnPackageResponse> assignToHashMap(final ReturnTokenResponse responses) {
        return responses.getDeliveryReturnSignatures().stream()
                .collect(Collectors.toMap(this::generateKeyFromResponse, Function.identity()));
    }

    private ShipmentId generateKeyFromResponse(final ReturnPackageResponse returnPackageResponse) {
        return !Objects.isNull(returnPackageResponse) ? returnPackageResponse.getShipmentId() : null;
    }
}
