package com.warehouse.deliveryreturn.domain.vo;


import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

import lombok.Builder;



@Builder
public class DeliveryReturnResponse {
    private final List<DeliveryReturnResponseDetails> deliveryReturnResponses;
    private final SupplierCode supplierCode;
    private final DepartmentCode departmentCode;

    public List<DeliveryReturnResponseDetails> getDeliveryReturnResponses() {
        return deliveryReturnResponses;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
