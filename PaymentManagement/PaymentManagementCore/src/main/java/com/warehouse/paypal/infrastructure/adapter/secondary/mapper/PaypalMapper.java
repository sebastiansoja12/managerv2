package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaypalMapper {

    @Mapping(target = "id", ignore = true)
    PaypalEntity map(PaymentInformation paymentInformation);

    @Mapping(source = "price", target = "details.subtotal")
    Amount map(AmountInformation amountInformation);
    @Mapping(target = "paypalId", source = "id")
    @Mapping(target = "payerId", ignore = true)
    PaymentInformation map(PaypalEntity paypalEntity);

    @Mapping(source = "telephoneNumber", target = "phone.nationalNumber")
    Payee map(PayeeInformation payeeInformation);

    PaypalResponse map(Payment payment);

}
