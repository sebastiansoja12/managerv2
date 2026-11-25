package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.vo.PaymentQuoteReference;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentPaymentReferenceDto;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentQuoteLookupServiceAdapterTest {

    @Test
    void shouldTreatLegacyShipmentWithoutPaymentStateAsNotPayable() {
        final ShipmentApiService shipmentApiService = mock(ShipmentApiService.class);
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-LEGACY");
        when(shipmentApiService.findPaymentReference(trackingNumber)).thenReturn(Optional.of(
                new ShipmentPaymentReferenceDto(OperatorId.of(10L), null, null, null)));

        final PaymentQuoteReference reference = new PaymentQuoteLookupServiceAdapter(shipmentApiService)
                .find(trackingNumber).orElseThrow();

        assertThat(reference.paymentRequired()).isFalse();
        assertThatThrownBy(reference::payableQuoteId)
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.NOT_FOUND));
    }

    @Test
    void shouldCarryBillingCustomerOfShipment() {
        final ShipmentApiService shipmentApiService = mock(ShipmentApiService.class);
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-SHOP");
        when(shipmentApiService.findPaymentReference(trackingNumber)).thenReturn(Optional.of(
                new ShipmentPaymentReferenceDto(OperatorId.of(10L), PaymentStateDto.UNPAID, new PriceQuoteId(3L),
                        new BillingCustomerId(55L))));

        final PaymentQuoteReference reference = new PaymentQuoteLookupServiceAdapter(shipmentApiService)
                .find(trackingNumber).orElseThrow();

        assertThat(reference.createdBy(new BillingCustomerId(55L))).isTrue();
        assertThat(reference.createdBy(new BillingCustomerId(56L))).isFalse();
    }
}
