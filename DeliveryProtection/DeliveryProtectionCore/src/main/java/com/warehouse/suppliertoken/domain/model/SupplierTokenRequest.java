package com.warehouse.suppliertoken.domain.model;

import lombok.Data;

import java.util.List;


@Data
public class SupplierTokenRequest {
    List<DeliveryPackageRequest> deliveryPackageList;
}