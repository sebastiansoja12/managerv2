package com.warehouse.deliveryreturn.domain.model;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliveryreturn.domain.vo.ReturnPackageRequest;
import com.warehouse.deliveryreturn.domain.vo.Supplier;

import lombok.Builder;


@Builder
public class ReturnTokenRequest {
    private List<ReturnPackageRequest> returnPackageRequests;
    private Supplier supplier;

    public ReturnTokenRequest(final List<ReturnPackageRequest> returnPackageRequests, final Supplier supplier) {
        this.returnPackageRequests = returnPackageRequests;
        this.supplier = supplier;
    }

    public List<ReturnPackageRequest> getReturnPackageRequests() {
        return returnPackageRequests;
    }

    public void setReturnPackageRequests(final List<ReturnPackageRequest> returnPackageRequests) {
        this.returnPackageRequests = returnPackageRequests;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(final Supplier supplier) {
        this.supplier = supplier;
    }

    public static ReturnTokenRequest from(final Set<DeliveryReturnDetails> deliveries,
                                          final DeviceInformation deviceInformation) {
        final List<ReturnPackageRequest> returnPackageRequests = deliveries
                .stream()
                .map(ReturnTokenRequest::createDeliveryPackageRequests)
                .flatMap(Collection::stream)
                .toList();
        final SupplierCode supplierCode = deliveries.stream()
                .map(DeliveryReturnDetails::getSupplierCode)
                .filter(ObjectUtils::isNotEmpty)
                .findAny()
                .orElseGet(() -> new SupplierCode(deviceInformation.getUsername()));
        final Supplier supplier = new Supplier(supplierCode);
        return ReturnTokenRequest.builder()
                .returnPackageRequests(returnPackageRequests)
                .supplier(supplier)
                .build();
    }

    private static List<ReturnPackageRequest> createDeliveryPackageRequests(final DeliveryReturnDetails delivery) {
        return List.of(createDeliveryPackageRequest(delivery));
    }

    private static ReturnPackageRequest createDeliveryPackageRequest(final DeliveryReturnDetails delivery) {
        return ReturnPackageRequest.builder()
                .deliveryStatus(delivery.getDeliveryStatus())
                .supplierCode(delivery.getSupplierCode())
                .departmentCode(delivery.getDepartmentCode())
                .shipmentId(delivery.getShipmentId())
                .locked(false)
                .build();
    }
}
