package com.warehouse.deliveryreturn.domain.port.primary;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.vo.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryReturnPortImpl implements DeliveryReturnPort {

    private final DeliveryReturnService deliveryReturnService;

    private final ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    private final RouteLogServicePort logServicePort;

    @Override
    public DeliveryReturnResponse deliverReturn(DeliveryReturnRequest deliveryRequest) {

        deliveryRequest.validateStatuses();

        final Set<DeliveryReturnDetails> deliveryReturnRequests = deliveryRequest.getDeliveries()
                .stream()
                .filter(Objects::nonNull)
                .map(DeliveryReturnDetails::updateDeliveryStatus)
                .collect(Collectors.toSet());

        final List<DeliveryReturn> deliveryReturnResponses =
                deliveryReturnService.deliverReturn(deliveryReturnRequests);

		final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails = deliveryReturnResponses.stream()
                .map(deliveryReturn -> {
					final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest
                            .builder()
							.parcelId(deliveryReturn.getParcelId())
                            .build();
					final UpdateStatus updateStatus = parcelStatusControlChangeServicePort
							.updateStatus(updateStatusParcelRequest);
                    return new DeliveryReturnResponseDetails(deliveryReturn.getId(), deliveryReturn.getParcelId(),
                            deliveryReturn.getDeliveryStatus(), updateStatus);
				}).collect(Collectors.toList());

        logRouteFlow();
		
		return DeliveryReturnResponse
                .builder()
                .deliveryReturnResponses(deliveryReturnResponseDetails)
                .build();
        
    }

    private void logRouteFlow() {
        //ogServicePort.logDeliverReturn();
    }
}
