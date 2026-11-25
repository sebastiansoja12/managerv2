package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.PaymentState;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;

import java.math.BigDecimal;

final class ShipmentFixture {

    static final PriceQuoteId PRICE_QUOTE_ID = new PriceQuoteId(77L);

    private ShipmentFixture() {
    }

    static Shipment unpaidCreated() {
        return shipment(ShipmentStatus.CREATED, PaymentState.UNPAID, PRICE_QUOTE_ID);
    }

    static Shipment unpaid(final ShipmentStatus shipmentStatus) {
        return shipment(shipmentStatus, PaymentState.UNPAID, PRICE_QUOTE_ID);
    }

    static Shipment paymentNotRequired(final ShipmentStatus shipmentStatus) {
        return shipment(shipmentStatus, PaymentState.NOT_REQUIRED, null);
    }

    static ShipmentWorkflowSettings closedCancellationWindow() {
        return new ShipmentWorkflowSettings(ShipmentStatus.CREATED, ShipmentServiceLevel.STANDARD, false, true,
                false, 0, "16:00");
    }

    private static Shipment shipment(final ShipmentStatus shipmentStatus, final PaymentState paymentState,
                                     final PriceQuoteId priceQuoteId) {
        return new Shipment(new ShipmentId(1L), party("Jan"), party("Anna"), null,
                new Money(new BigDecimal("24.60"), Currency.PLN), false, ShipmentPriority.MEDIUM,
                new TrackingNumber("MGR-1"), shipmentStatus,
                new Dimensions(new BigDecimal("20"), new BigDecimal("20"), new BigDecimal("20"), LengthUnit.CM),
                new Weight(new BigDecimal("2"), WeightUnit.KG), null, null, null, ShipmentServiceLevel.STANDARD,
                null, ShipmentSource.WEB, paymentState, priceQuoteId, null);
    }

    private static Party party(final String firstName) {
        return Party.builder()
                .firstName(firstName)
                .lastName("Kowalski")
                .email("test@test.pl")
                .telephoneNumber("123456789")
                .city("Warszawa")
                .street("Prosta 1")
                .postalCode("00-001")
                .countryCode(CountryCode.PL)
                .build();
    }
}
