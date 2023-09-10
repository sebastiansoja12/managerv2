package com.warehouse.paypal.infrastructure.adapter.primary.mapper;

import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaymentRequestMapper {
    @Mapping(source = "parcelId.value", target = "parcelId")
    @Mapping(source = "price.value", target = "price")
    PaymentRequest map(PaymentRequestDto request);
}
