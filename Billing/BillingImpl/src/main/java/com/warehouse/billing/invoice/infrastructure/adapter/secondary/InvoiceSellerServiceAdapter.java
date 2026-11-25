package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceSellerServicePort;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.domain.vo.InvoiceSeller;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentPaymentConfigurationDto;

import java.util.Optional;
import java.util.function.Function;

public class InvoiceSellerServiceAdapter implements InvoiceSellerServicePort {

    private final OperatorApiService operatorApiService;

    private final OperatorContextProvider operatorContextProvider;

    public InvoiceSellerServiceAdapter(final OperatorApiService operatorApiService,
                                       final OperatorContextProvider operatorContextProvider) {
        this.operatorApiService = operatorApiService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public InvoiceSeller currentSeller() {
        final OperatorDto operator = this.operatorApiService.getById(this.operatorContextProvider.currentOperatorId());
        final Optional<ShipmentPaymentConfigurationDto> billing = Optional.ofNullable(operator.configuration())
                .map(OperatorConfigurationDto::shipmentConfiguration)
                .map(ShipmentConfigurationDto::paymentConfiguration);
        return new InvoiceSeller(new InvoiceParty(operator.companyName(), operator.taxId(),
                billing(billing, ShipmentPaymentConfigurationDto::billingStreet),
                billing(billing, ShipmentPaymentConfigurationDto::billingPostalCode),
                billing(billing, ShipmentPaymentConfigurationDto::billingCity),
                billing(billing, ShipmentPaymentConfigurationDto::billingCountryCode),
                operator.contactEmail()),
                billing(billing, ShipmentPaymentConfigurationDto::bankAccount));
    }

    private String billing(final Optional<ShipmentPaymentConfigurationDto> billing,
                           final Function<ShipmentPaymentConfigurationDto, String> field) {
        return billing.map(field).orElse(null);
    }
}
