package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.api.dto.BillingCustomerDto;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceBuyerServicePort;
import com.warehouse.billing.invoice.domain.vo.InvoiceBuyer;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentBillingPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceBuyerServiceAdapter implements InvoiceBuyerServicePort {

    private final ShipmentApiService shipmentApiService;

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    private final BillingCustomerApiService billingCustomerApiService;

    public InvoiceBuyerServiceAdapter(final ShipmentApiService shipmentApiService,
                                      final OperatorConfigurationApiService operatorConfigurationApiService,
                                      final BillingCustomerApiService billingCustomerApiService) {
        this.shipmentApiService = shipmentApiService;
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.billingCustomerApiService = billingCustomerApiService;
    }

    @Override
    public InvoiceBuyer findBuyer(final PriceQuoteId priceQuoteId, final TaxId buyerTaxId) {
        return this.shipmentApiService.findBillingParty(priceQuoteId)
                .flatMap(party -> party(party, buyerTaxId))
                .map(party -> new InvoiceBuyer(party, language()))
                .orElseGet(() -> new InvoiceBuyer(InvoiceParty.unknown(buyerTaxId), null));
    }

    private Optional<InvoiceParty> party(final ShipmentBillingPartyDto party, final TaxId buyerTaxId) {
        if (party.billingCustomerId() != null) {
            return this.billingCustomerApiService.findById(party.billingCustomerId()).map(this::billingCustomer);
        }
        return Optional.ofNullable(party.sender()).map(sender -> sender(sender, buyerTaxId));
    }

    private InvoiceParty billingCustomer(final BillingCustomerDto billingCustomer) {
        return new InvoiceParty(billingCustomer.companyName(), billingCustomer.taxId(), billingCustomer.street(),
                billingCustomer.postalCode(), billingCustomer.city(), billingCustomer.countryCode(),
                billingCustomer.invoiceEmail());
    }

    private InvoiceParty sender(final ShipmentPartyDto sender, final TaxId buyerTaxId) {
        final String name = Stream.of(sender.firstName(), sender.lastName())
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
        return new InvoiceParty(name.isBlank() ? null : name, buyerTaxId, sender.street(), sender.postalCode(),
                sender.city(), sender.countryCode(), sender.email());
    }

    private String language() {
        return Optional.ofNullable(this.operatorConfigurationApiService.getCurrentCommunicationConfiguration())
                .map(CommunicationConfigurationDto::defaultCustomerLanguage)
                .map(language -> language.name().toLowerCase(Locale.ROOT))
                .orElse(null);
    }
}
