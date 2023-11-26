package com.warehouse.deliveryreturn;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.deliveryreturn.configuration.DeliveryReturnTestConfiguration;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;

@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DatabaseSetup("/deliveryreturn/repository/shipment.xml")
public class ParcelRepositoryServiceAdapterIntegrationTest {

    @Autowired
    private ParcelRepositoryServicePort parcelRepositoryServicePort;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ParcelStatusProperty parcelStatusProperty;

    @Test
    void shouldNotDownloadParcel() {
        // given
        final Long parcelId = 1L;
        // when
        final Parcel parcel = parcelRepositoryServicePort.downloadParcel(parcelId);
        // then
        assertNull(parcel);
    }

}
