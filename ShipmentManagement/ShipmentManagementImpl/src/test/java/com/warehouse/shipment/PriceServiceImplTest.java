package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;
import com.warehouse.shipment.domain.service.PriceServiceImpl;
import com.warehouse.shipment.domain.vo.Price;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @Test
    void shouldDetermineShipmentPrice() {
        final PriceServiceImpl service = new PriceServiceImpl(priceRepository);
        final Price expectedPrice = new Price(BigDecimal.TEN, Currency.PLN);
        when(priceRepository.priceByShipmentSize(ShipmentSize.SMALL, Currency.PLN)).thenReturn(expectedPrice);

        final Price price = service.determineShipmentPrice(ShipmentSize.SMALL, Currency.PLN);

        assertEquals(expectedPrice, price);
        verify(priceRepository).priceByShipmentSize(ShipmentSize.SMALL, Currency.PLN);
    }
}
