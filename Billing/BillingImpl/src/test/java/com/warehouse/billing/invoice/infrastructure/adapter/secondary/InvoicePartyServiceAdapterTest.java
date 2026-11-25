package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.domain.vo.InvoiceBuyer;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.domain.vo.InvoiceSeller;
import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.api.dto.BillingCustomerDto;
import com.warehouse.billing.api.dto.BillingCustomerStatusDto;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.CashOnDeliveryPaymentMethodDto;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.CustomerLanguageDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentPaymentConfigurationDto;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentBillingPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.BUYER_TAX_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.PRICE_QUOTE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InvoicePartyServiceAdapterTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    private final ShipmentApiService shipmentApiService = mock(ShipmentApiService.class);

    private final OperatorConfigurationApiService operatorConfigurationApiService =
            mock(OperatorConfigurationApiService.class);

    private final OperatorApiService operatorApiService = mock(OperatorApiService.class);

    private final OperatorContextProvider operatorContextProvider = () -> Optional.of(
            new OperatorDetails(OPERATOR_ID, new UserId(7L), new DepartmentId(100L)));

    private final BillingCustomerApiService billingCustomerApiService = mock(BillingCustomerApiService.class);

    private final InvoiceBuyerServiceAdapter buyerAdapter = new InvoiceBuyerServiceAdapter(this.shipmentApiService,
            this.operatorConfigurationApiService, this.billingCustomerApiService);

    private final InvoiceSellerServiceAdapter sellerAdapter = new InvoiceSellerServiceAdapter(
            this.operatorApiService, this.operatorContextProvider);

    @Test
    void shouldInvoiceShipmentSenderWithCounterTaxId() {
        when(this.shipmentApiService.findBillingParty(PRICE_QUOTE_ID)).thenReturn(Optional.of(
                new ShipmentBillingPartyDto(new ShipmentPartyDto("Jan", "Kowalski", "jan@example.com", "500600700",
                        "Kraków", "31-147", "Długa 1", "PL"), null)));
        when(this.operatorConfigurationApiService.getCurrentCommunicationConfiguration()).thenReturn(
                new CommunicationConfigurationDto(null, null, null, null, null, CustomerLanguageDto.DE, 30, false, 5));

        final InvoiceBuyer buyer = this.buyerAdapter.findBuyer(PRICE_QUOTE_ID, BUYER_TAX_ID);

        assertThat(buyer.party()).isEqualTo(new InvoiceParty("Jan Kowalski", BUYER_TAX_ID, "Długa 1", "31-147",
                "Kraków", "PL", "jan@example.com"));
        assertThat(buyer.language()).isEqualTo("de");
    }

    @Test
    void shouldInvoiceBillingCustomerWhenShopCreatedShipment() {
        final BillingCustomerId customerId = new BillingCustomerId(55L);
        when(this.shipmentApiService.findBillingParty(PRICE_QUOTE_ID)).thenReturn(Optional.of(
                new ShipmentBillingPartyDto(new ShipmentPartyDto("Jan", "Kowalski", "jan@example.com", "500600700",
                        "Kraków", "31-147", "Długa 1", "PL"), customerId)));
        when(this.billingCustomerApiService.findById(customerId)).thenReturn(Optional.of(new BillingCustomerDto(
                customerId, OPERATOR_ID, "Sklep ABC sp. z o.o.", new TaxId("5260250995"), "Krótka 2", "00-001",
                "Warszawa", "PL", "faktury@sklep.pl", BillingCustomerStatusDto.ACTIVE, new UserId(70L))));
        when(this.operatorConfigurationApiService.getCurrentCommunicationConfiguration()).thenReturn(
                new CommunicationConfigurationDto(null, null, null, null, null, CustomerLanguageDto.PL, 30, false, 5));

        final InvoiceBuyer buyer = this.buyerAdapter.findBuyer(PRICE_QUOTE_ID, BUYER_TAX_ID);

        assertThat(buyer.party()).isEqualTo(new InvoiceParty("Sklep ABC sp. z o.o.", new TaxId("5260250995"),
                "Krótka 2", "00-001", "Warszawa", "PL", "faktury@sklep.pl"));
        assertThat(buyer.language()).isEqualTo("pl");
    }

    @Test
    void shouldLeaveBuyerUnknownWhenShipmentIsMissing() {
        when(this.shipmentApiService.findBillingParty(PRICE_QUOTE_ID)).thenReturn(Optional.empty());

        final InvoiceBuyer buyer = this.buyerAdapter.findBuyer(PRICE_QUOTE_ID, BUYER_TAX_ID);

        assertThat(buyer.party()).isEqualTo(new InvoiceParty(null, BUYER_TAX_ID, null, null, null, null, null));
        assertThat(buyer.party().email()).isNull();
    }

    @Test
    void shouldReadSellerBillingSettingsFromOperator() {
        when(this.operatorApiService.getById(OPERATOR_ID)).thenReturn(operator(new ShipmentPaymentConfigurationDto(
                true, 5000.0, CashOnDeliveryPaymentMethodDto.BOTH, 0.0, 72, true, "ul. Żółkiewskiego 5", "00-950",
                "Łódź", "PL", "PL61109010140000071219812874")));

        final InvoiceSeller seller = this.sellerAdapter.currentSeller();

        assertThat(seller.party()).isEqualTo(new InvoiceParty("Szybka Paczka", TaxId.of("1234563218"),
                "ul. Żółkiewskiego 5", "00-950", "Łódź", "PL", "biuro@example.com"));
        assertThat(seller.bankAccount()).isEqualTo("PL61109010140000071219812874");
    }

    @Test
    void shouldIssueSellerWithEmptyAddressWhenBillingSettingsAreMissing() {
        when(this.operatorApiService.getById(OPERATOR_ID)).thenReturn(operator(null));

        final InvoiceSeller seller = this.sellerAdapter.currentSeller();

        assertThat(seller.party()).isEqualTo(new InvoiceParty("Szybka Paczka", TaxId.of("1234563218"),
                null, null, null, null, "biuro@example.com"));
        assertThat(seller.bankAccount()).isNull();
    }

    private static OperatorDto operator(final ShipmentPaymentConfigurationDto paymentConfiguration) {
        return new OperatorDto(OPERATOR_ID, null, TaxId.of("1234563218"), false, false, false, null,
                "biuro@example.com", "Szybka Paczka", null, null, null,
                new OperatorConfigurationDto(null, new ShipmentConfigurationDto(null, null, null, null, null, null,
                        null, null, paymentConfiguration, null, null), null),
                null, null, null);
    }
}
