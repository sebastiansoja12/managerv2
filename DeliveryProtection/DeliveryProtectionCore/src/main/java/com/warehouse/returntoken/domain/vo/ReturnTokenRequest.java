package com.warehouse.returntoken.domain.vo;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.client.RestClientException;

import com.google.common.collect.Maps;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.returntoken.domain.model.ReturnPackageRequest;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Builder
public class ReturnTokenRequest {
    private final List<ReturnPackageRequest> returnPackageRequests;
    private final Supplier supplier;

    public ReturnTokenRequest(final List<ReturnPackageRequest> returnPackageRequests, final Supplier supplier) {
        this.returnPackageRequests = returnPackageRequests;
        this.supplier = supplier;
    }

    public List<ReturnPackageRequest> getReturnPackageRequests() {
        return returnPackageRequests;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void checkAllShipmentsHaveCorrectDeliveryStatus() {
        for (final ReturnPackageRequest returnPackageRequest : returnPackageRequests) {
            if (!returnPackageRequest.getDeliveryStatus().equals(DeliveryStatus.RETURN)) {
                throw new RestClientException("Wrong delivery status for shipment {} " + returnPackageRequest.getShipmentId());
            }
        }
    }

    public Map<ShipmentId, CrossCourierDelivery> checkCrossSupplierDelivery() {
        final SupplierCode deviceSupplierCode = supplier.getSupplierCode();
        final Map<ShipmentId, CrossCourierDelivery> crossCourierDeliveryMap = Maps.newHashMap();
        for (final ReturnPackageRequest returnPackageRequest : this.returnPackageRequests) {
            if (ObjectUtils.notEqual(deviceSupplierCode, returnPackageRequest.getSupplierCode())) {
                crossCourierDeliveryMap.put(returnPackageRequest.getShipmentId(),
                        new CrossCourierDelivery(returnPackageRequest.getSupplierCode(), supplier.getSupplierCode()));
            }
        }
        return crossCourierDeliveryMap;
    }

    public List<ReturnPackageRequest> filterOutAllLockedShipments() {
        return this.returnPackageRequests
                .stream()
                .filter(returnPackageRequest -> !returnPackageRequest.isLocked())
                .toList();
    }

}
