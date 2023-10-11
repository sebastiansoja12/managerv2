package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.shipment.domain.model.PaymentStatus;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaymentMapper {

    PaymentRequest map(com.warehouse.shipment.domain.vo.PaymentRequest request);

    @Mapping(source = "link.value", target = "link")
    PaymentStatus map(PaymentResponse paymentResponse);
}
