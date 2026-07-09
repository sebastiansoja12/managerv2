package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;
import com.warehouse.shipment.domain.vo.Price;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ShipmentReadRepositoryTest.ShipmentReadRepositoryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/database/price.xml")
public class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @ParameterizedTest
    @CsvSource({
            "AVERAGE, 10.00, EUR",
            "BIG, 15.00, EUR",
            "MEDIUM, 20.00, EUR",
    })
    void shouldGetPriceBySize(final ShipmentSize shipmentSize, final BigDecimal amount, final Currency currency) {
        final Price price = priceRepository.priceByShipmentSize(shipmentSize, currency);
        assertAll(
                () -> assertEquals(price.price(), amount),
                () -> assertEquals(price.currency(), currency)
        );
    }
}
