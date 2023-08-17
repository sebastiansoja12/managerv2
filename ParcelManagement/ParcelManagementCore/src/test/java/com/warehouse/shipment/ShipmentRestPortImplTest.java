package com.warehouse.shipment;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.port.primary.ShipmentRestPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShipmentRestPortImplTest {


    @Autowired
    private ShipmentRestPort shipmentRestPort;

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldDeleteParcelFromDatabase() {
        // given
        final Long parcelId = 100001L;
        // when
        shipmentRestPort.delete(parcelId);
        // and
        final Executable executable = () -> shipmentRestPort.loadParcel(parcelId);
        // then
        final ParcelNotFoundException exception =
                assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel was not found"), exception.getMessage());
    }

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldNotDeleteParcelFromDatabase() {
        // given
        final Long parcelId = 0L;
        // when
        shipmentRestPort.delete(parcelId);
        // and
        final Parcel parcel = shipmentRestPort.loadParcel(100001L);
        // then
        assertThat(parcel).isNotNull();
    }

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldLoadParcel() {
        // given
        final Long parcelId = 100001L;
        // when
        final Parcel parcel = shipmentRestPort.loadParcel(parcelId);
        // then
        assertThat(parcel).isNotNull();
    }

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldNotLoadParcel() {
        // given
        final Long parcelId = 0L;
        // when
        final Executable executable = () -> shipmentRestPort.loadParcel(parcelId);
        // then
        final ParcelNotFoundException exception =
                assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel was not found"), exception.getMessage());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
