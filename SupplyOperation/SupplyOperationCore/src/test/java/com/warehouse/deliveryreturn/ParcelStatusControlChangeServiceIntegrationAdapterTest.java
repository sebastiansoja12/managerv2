package com.warehouse.deliveryreturn;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.deliveryreturn.configuration.DeliveryReturnTestConfiguration;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;

@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ParcelStatusControlChangeServiceIntegrationAdapterTest {

    @Autowired
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ParcelStatusProperty parcelStatusProperty;

    @Test
    void shouldNotUpdateParcelStatus() {
        // given
        final UpdateStatusParcelRequest parcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();
        // when
        final UpdateStatus updateStatus = parcelStatusControlChangeServicePort.updateStatus(parcelRequest);
        // then
        assertEquals(UpdateStatus.NOT_OK, updateStatus);
    }
}
