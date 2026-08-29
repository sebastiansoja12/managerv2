package com.warehouse.qrcode;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.ShipmentRepository;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.qrcode.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = QRCodeTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ShipmentRepositoryImplTest {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void shouldFindParcelById() {
        final Shipment shipment = shipmentRepository.find(new ShipmentId(1L));
        assertNotNull(shipment);
    }

    @Test
    void shouldNotFindParcelById() {
        final Executable executable = () -> shipmentRepository.find(new ShipmentId(2L));
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals("Shipment 2 was not found", exception.getMessage());
    }
}
