package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.shipment.domain.model.PaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaymentMapper {

    PaymentRequest map(com.warehouse.shipment.domain.vo.PaymentRequest request);

    @Mapping(source = "paymentUrl", target = "link.paymentUrl")
    PaymentResponse map(com.warehouse.shipment.domain.vo.PaymentResponse paymentResponse);

    @Mapping(source = "link.paymentUrl", target = "link")
    PaymentStatus map(PaymentResponse paymentResponse);
}
