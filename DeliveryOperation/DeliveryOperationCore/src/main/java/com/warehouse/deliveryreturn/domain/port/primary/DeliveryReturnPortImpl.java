package com.warehouse.deliveryreturn.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.warehouse.deliveryreturn.domain.exception.DeliveryRequestException;
import com.warehouse.deliveryreturn.domain.exception.DeliveryReturnDetailsException;
import com.warehouse.deliveryreturn.domain.exception.WrongProcessTypeException;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogReturnServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentStatusControlServicePort;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.vo.*;
import com.warehouse.terminal.DeviceInformation;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DeliveryReturnPortImpl implements DeliveryReturnPort {

    private final DeliveryReturnService deliveryReturnService;

    private final ShipmentStatusControlServicePort shipmentStatusControlServicePort;

    private final RouteLogReturnServicePort routeLogReturnServicePort;

    public DeliveryReturnPortImpl(final DeliveryReturnService deliveryReturnService,
                                  final ShipmentStatusControlServicePort shipmentStatusControlServicePort,
                                  final RouteLogReturnServicePort routeLogReturnServicePort) {
        this.deliveryReturnService = deliveryReturnService;
        this.shipmentStatusControlServicePort = shipmentStatusControlServicePort;
        this.routeLogReturnServicePort = routeLogReturnServicePort;
    }

    @Override
    public DeliveryReturnResponse deliverReturn(final DeliveryReturnRequest deliveryReturnRequest) {

        validateRequest(deliveryReturnRequest);
        
        if (!deliveryReturnRequest.isReturnProcessType()) {
            logWrongProcessTypeInformation();
            throw new WrongProcessTypeException(403, "Wrong process type");
        }

        deliveryReturnRequest.validateStatuses();

        deliveryReturnRequest.rewriteSupplierCodeFromDevice();

        deliveryReturnRequest.rewriteDepartmentCodeFromDevice();

        final Set<DeliveryReturnDetails> deliveryReturnRequests = deliveryReturnRequest.getDeliveryReturnDetails()
                .stream()
                .filter(Objects::nonNull)
                .map(DeliveryReturnDetails::updateDeliveryStatus)
                .collect(Collectors.toSet());

        final DeviceInformation deviceInformation = deliveryReturnRequest.getDeviceInformation();

        final List<DeliveryReturn> deliveryReturns =
                deliveryReturnService.deliverReturn(deliveryReturnRequests, deviceInformation);

		final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails = deliveryReturns.stream()
                .map(deliveryReturn -> {
					final UpdateStatusShipmentRequest updateStatusShipmentRequest = new UpdateStatusShipmentRequest(
							deliveryReturn.getShipmentId());
					final UpdateStatus updateStatus = shipmentStatusControlServicePort
							.updateStatus(updateStatusShipmentRequest);
                    return DeliveryReturnResponseDetails.from(deliveryReturn, updateStatus);
				}).toList();

		return DeliveryReturnResponse
                .builder()
                .deliveryReturnResponseDetails(deliveryReturnResponseDetails)
                .deviceInformation(deliveryReturnRequest.getDeviceInformation())
                .build();
    }

    private void validateRequest(final DeliveryReturnRequest deliveryRequest) {
        if (Objects.isNull(deliveryRequest)) {
            throw new DeliveryRequestException("Delivery return request cannot be null");
        }
        if (CollectionUtils.isEmpty(deliveryRequest.getDeliveryReturnDetails())) {
            throw new DeliveryReturnDetailsException("Delivery return details cannot be null");
        }
    }

    private void logWrongProcessTypeInformation() {
        log.warn("Process type is different than RETURN");
    }
}