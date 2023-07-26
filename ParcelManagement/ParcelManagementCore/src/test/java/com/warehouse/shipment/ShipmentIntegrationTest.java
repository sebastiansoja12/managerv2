package com.warehouse.shipment;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShipmentIntegrationTest {

    @Autowired
    private ShipmentPort shipmentPort;

    @Test
    void shouldShipParcel() {

    }


    @Test
    void shouldUpdateParcel() {
        // TODO
    }

}
