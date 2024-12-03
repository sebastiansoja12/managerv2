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
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class DeliveryReturnPortImpl implements DeliveryReturnPort {

    private final DeliveryReturnService deliveryReturnService;

    private final ShipmentStatusControlServicePort shipmentStatusControlServicePort;

    private final RouteLogReturnServicePort routeLogReturnServicePort;

    @Override
    public DeliveryReturnResponse deliverReturn(final DeliveryReturnRequest deliveryRequest) {

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

        logSupplierCode(deliveryReturnResponses);

//		final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails = deliveryReturnResponses.stream()
//                .map(deliveryReturn -> {
//					final UpdateStatusParcelRequest updateStatusParcelRequest = new UpdateStatusParcelRequest(
//							deliveryReturn.getParcelId());
//					final UpdateStatus updateStatus = shipmentStatusControlServicePort
//							.updateStatus(updateStatusParcelRequest);
//                    return new DeliveryReturnResponseDetails(deliveryReturn.getId(), deliveryReturn.getParcelId(),
//                            deliveryReturn.getDeliveryStatus(), deliveryReturn.getToken(), updateStatus);
//				}).collect(Collectors.toList());

		return DeliveryReturnResponse.builder().build();
    }

    private void logDepotCode(final DeliveryReturnRequest deliveryRequest) {
        log.warn("Logging depot code {} in tracker", deliveryRequest.getDepotCode());
        routeLogReturnServicePort.logDepotCodeReturnDelivery(deliveryRequest);
    }

    private void validateRequest(final DeliveryReturnRequest deliveryRequest) {
        if (Objects.isNull(deliveryRequest)) {
            throw new DeliveryRequestException("Delivery return request cannot be null");
        }
        if (CollectionUtils.isEmpty(deliveryRequest.getDeliveryReturnDetails())) {
            throw new DeliveryReturnDetailsException("Delivery return details cannot be null");
        }
    }
    
	private void logSupplierCode(List<DeliveryReturn> deliveryReturns) {
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