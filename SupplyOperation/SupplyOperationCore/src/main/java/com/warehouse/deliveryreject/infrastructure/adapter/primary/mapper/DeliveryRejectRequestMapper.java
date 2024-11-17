package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryRejectRequestMapper {
    DeliveryRejectRequest map(final DeliveryRejectRequestDto deliveryRejectRequest);
    DeliveryRejectRequest map(final DeliveryRequest deliveryRequest);

    SupplierCode map(final String value);
}
