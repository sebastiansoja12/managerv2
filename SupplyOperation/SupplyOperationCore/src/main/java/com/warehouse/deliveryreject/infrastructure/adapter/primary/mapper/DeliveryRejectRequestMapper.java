package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryRejectRequestMapper {
    DeliveryRejectRequest map(final DeliveryRejectRequestDto deliveryRejectRequest);

    List<DeliveryRejectRequest> map(final List<DeliveryRejectRequestDto> deliveryRejectRequestDtoList);

    SupplierCode map(final String value);
}
