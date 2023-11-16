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
import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import com.warehouse.qrcode.infrastructure.adapter.secondary.exception.ParcelNotFoundException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = QRCodeTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ParcelRepositoryImplTest {

    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    void shouldFindParcelById() {
        // when
        final Parcel parcel = parcelRepository.find(1L);
        // then
        assertNotNull(parcel);
    }

    @Test
    void shouldNotFindParcelById() {
        // when
        final Executable executable = () -> parcelRepository.find(2L);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals("Parcel 2 was not found", exception.getMessage());
    }
}
