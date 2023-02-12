package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payee;
import com.warehouse.paypal.domain.model.AmountInformation;
import com.warehouse.paypal.domain.model.PayeeInformation;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface PaypalMapper {

    PaypalEntity map(PaymentInformation paymentInformation);

    @Mapping(source = "price", target = "details.subtotal")
    Amount map(AmountInformation amountInformation);

    PaymentInformation map(PaypalEntity paypalEntity);

    @Mapping(source = "telephoneNumber", target = "phone.nationalNumber")
    Payee map(PayeeInformation payeeInformation);

}
