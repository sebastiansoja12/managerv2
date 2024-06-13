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
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogReturnServicePort;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.vo.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteDetails;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class DeliveryReturnPortImpl implements DeliveryReturnPort {

    private final DeliveryReturnService deliveryReturnService;

    private final ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    private final RouteLogReturnServicePort routeLogReturnServicePort;

    @Override
    public DeliveryReturnResponse deliverReturn(DeliveryReturnRequest deliveryRequest) {

        validateRequest(deliveryRequest);
        
        if (!deliveryRequest.isReturnProcessType()) {
            logWrongProcessTypeInformation();
            throw new WrongProcessTypeException(403, "Wrong process type");
        }

        deliveryRequest.validateStatuses();

        deliveryRequest.rewriteSupplierCodeFromDevice();

        deliveryRequest.rewriteDepotCodeFromDevice();

        logDepotCode(deliveryRequest);

        final Set<DeliveryReturnDetails> deliveryReturnRequests = deliveryRequest.getDeliveryReturnDetails()
                .stream()
                .filter(Objects::nonNull)
                .map(DeliveryReturnDetails::updateDeliveryStatus)
                .collect(Collectors.toSet());

        final List<DeliveryReturn> deliveryReturnResponses =
                deliveryReturnService.deliverReturn(deliveryReturnRequests);

        saveSupplierCode(deliveryReturnResponses);

		final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails = deliveryReturnResponses.stream()
                .map(deliveryReturn -> {
					final UpdateStatusParcelRequest updateStatusParcelRequest = new UpdateStatusParcelRequest(
							deliveryReturn.getParcelId());
					final UpdateStatus updateStatus = parcelStatusControlChangeServicePort
							.updateStatus(updateStatusParcelRequest);
                    return new DeliveryReturnResponseDetails(deliveryReturn.getId(), deliveryReturn.getParcelId(),
                            deliveryReturn.getDeliveryStatus(), deliveryReturn.getToken(), updateStatus);
				}).collect(Collectors.toList());
		
		final DeliveryReturnResponse deliveryReturnResponse = DeliveryReturnResponse
                .builder()
				.deliveryReturnResponses(deliveryReturnResponseDetails)
                .supplierCode(deliveryRequest.getDeviceInformation().getUsername())
                .depotCode(deliveryRequest.getDeviceInformation().getDepotCode())
                .build();

        logRouteFlow(deliveryReturnResponse);
        
        return deliveryReturnResponse;
    }

    private void logDepotCode(DeliveryReturnRequest deliveryRequest) {
        log.warn("Logging depot code {} in tracker", deliveryRequest.getDepotCode());
        routeLogReturnServicePort.logDepotCodeReturnDelivery(deliveryRequest);
    }

    private void validateRequest(DeliveryReturnRequest deliveryRequest) {
        if (Objects.isNull(deliveryRequest)) {
            throw new DeliveryRequestException("Delivery return request cannot be null");
        }
        if (CollectionUtils.isEmpty(deliveryRequest.getDeliveryReturnDetails())) {
            throw new DeliveryReturnDetailsException("Delivery return details cannot be null");
        }
    }

    private void logRouteFlow(DeliveryReturnResponse deliveryReturnResponse) {
        final DeliveryReturnRouteRequest request = DeliveryReturnRouteRequest.builder()
                .depotCode(deliveryReturnResponse.getDepotCode())
                .username(deliveryReturnResponse.getSupplierCode())
				.deliveryReturnRouteDetails(
						deliveryReturnRouteDetails(deliveryReturnResponse.getDeliveryReturnResponses()))
                .supplierCode(deliveryReturnResponse.getSupplierCode())
                .build();
        routeLogReturnServicePort.logRouteLogReturnDelivery(request);
    }
    
	private List<DeliveryReturnRouteDetails> deliveryReturnRouteDetails(
			List<DeliveryReturnResponseDetails> deliveryReturnResponses) {
        return deliveryReturnResponses.stream()
                .map(this::convertToDeliveryReturnRouteDetails)
                .collect(Collectors.toList());
    }

	private DeliveryReturnRouteDetails convertToDeliveryReturnRouteDetails(DeliveryReturnResponseDetails response) {
        return DeliveryReturnRouteDetails.builder()
                .deliveryStatus(response.getDeliveryStatus())
                .id(response.getId())
                .parcelId(response.getParcelId())
                .updateStatus(response.getUpdateStatus().name())
                .returnToken(response.getReturnToken())
                .build();
    }
    
	private void saveSupplierCode(List<DeliveryReturn> deliveryReturns) {
		deliveryReturns.forEach(deliveryReturn -> {
			logSupplierCodeSave(deliveryReturn.getSupplierCode());
			routeLogReturnServicePort.logSupplierCode(deliveryReturn);
		});
	}

    private void logWrongProcessTypeInformation() {
        log.warn("Process type is different than RETURN");
    }
    
    private void logSupplierCodeSave(String supplierCode) {
        log.warn("Saving supplier code {} in process", supplierCode);
    }
}

