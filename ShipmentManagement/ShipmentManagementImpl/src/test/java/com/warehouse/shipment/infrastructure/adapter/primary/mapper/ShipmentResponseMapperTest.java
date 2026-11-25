package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.enumeration.PaymentState;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PaymentStateDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PriceQuoteIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentResponseMapperTest {

    private final ShipmentResponseMapper mapper = Mappers.getMapper(ShipmentResponseMapper.class);

    @Test
    void shouldExposePaymentStateAndPriceQuote() {
        final Party party = Party.builder().firstName("Jan").lastName("Nowak").email("jan@test.pl")
                .telephoneNumber("123456789").city("Warszawa").street("Prosta 1").postalCode("00-001")
                .countryCode(CountryCode.PL).build();
        final Shipment shipment = new Shipment(new ShipmentId(1L), party, party, null,
                new Money(new BigDecimal("24.60"), Currency.PLN), false, ShipmentPriority.MEDIUM,
                new TrackingNumber("MGR-1"), ShipmentStatus.CREATED,
                new Dimensions(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, LengthUnit.CM),
                new Weight(BigDecimal.ONE, WeightUnit.KG), null, null, null, ShipmentServiceLevel.STANDARD, null,
                ShipmentSource.WEB, PaymentState.UNPAID, new PriceQuoteId(77L), null);

        final ShipmentDto response = this.mapper.map(new ShipmentResult(shipment.snapshot()));

        assertThat(response.getPaymentState()).isEqualTo(PaymentStateDto.UNPAID);
        assertThat(response.getPriceQuoteId()).isEqualTo(new PriceQuoteIdDto(77L));
    }
}
