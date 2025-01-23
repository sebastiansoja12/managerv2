package com.warehouse.returntoken.domain.port.primary;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.model.ReturnPackageRequest;
import com.warehouse.returntoken.domain.service.ReturnTokenService;
import com.warehouse.returntoken.domain.vo.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenPortImpl implements ReturnTokenPort {

    private final ReturnTokenService returnTokenService;

    private final Logger log = LoggerFactory.getLogger("ReturnToken");

    @Override
    public ReturnTokenResponse signWith(final ReturnTokenRequest request) {

        log.info("Signing with request {}", request);

        final Supplier deviceSupplier = request.getSupplier();

        request.checkAllShipmentsHaveCorrectDeliveryStatus();

        final Map<ShipmentId, CrossCourierDelivery> crossCourierDeliveryMap = request.checkCrossSupplierDelivery();

        final List<ReturnPackageRequest> returnPackageRequests = request.filterOutAllLockedShipments();

        final Set<ReturnPackageResponse> returnPackageResponses = Sets.newHashSet();
        for (final ReturnPackageRequest returnPackageRequest : returnPackageRequests) {
            final ReturnPackageResponse returnPackageResponse = this.returnTokenService.sign(returnPackageRequest);
            returnPackageResponses.add(returnPackageResponse);
        }

        final List<ReturnPackageResponse> filteredPackageResponses = returnPackageResponses
                .stream()
                .filter(returnPackageResponse -> returnPackageResponse.getReturnToken() != null)
                .sorted()
                .peek(returnPackageResponse -> {
                    final ShipmentId shipmentId = returnPackageResponse.getShipmentId();
                    final CrossCourierDelivery delivery = crossCourierDeliveryMap.get(shipmentId);
                    if (delivery != null) {
                        returnPackageResponse.updateCrossCourierDelivery(delivery);
                    } else {
                        logNoCrossCourierDeliveryFound(shipmentId);
                    }
                })
                .toList();


        log.info("Generated return tokens for shipments: {}", filteredPackageResponses
                .stream()
                .map(ReturnPackageResponse::getShipmentId)
                .toList());


        return new ReturnTokenResponse(filteredPackageResponses, deviceSupplier);
    }

    private void logNoCrossCourierDeliveryFound(final ShipmentId shipmentId) {
        log.info("No cross courier delivery found for shipmentId {}", shipmentId);
    }

    @Override
    public Boolean verify(final ReturnToken returnToken) {
        return returnTokenService.exists(returnToken);
    }

    @Override
    public ReturnToken getReturnToken(final ShipmentId shipmentId) {
        return returnTokenService.findByShipmentId(shipmentId);
    }
}
