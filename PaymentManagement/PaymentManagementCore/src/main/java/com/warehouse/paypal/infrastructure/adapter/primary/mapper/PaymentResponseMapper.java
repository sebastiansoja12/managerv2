package com.warehouse.paypal.infrastructure.adapter.primary.mapper;

import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.model.PaymentUpdateResponse;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentResponseDto;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentUpdateResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentResponseMapper {
    PaymentResponseDto map(PaymentResponse response);

    PaymentUpdateResponseDto map(PaymentUpdateResponse response);

}