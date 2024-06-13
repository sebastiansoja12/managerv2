package com.warehouse.deliveryreturn;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ParcelStatusControlChangeServiceIntegrationAdapterTest {

    @Autowired
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Autowired
    private RestClient restClient;

    @Test
    void shouldNotUpdateParcelStatus() {
        // given
        final UpdateStatusParcelRequest parcelRequest = new UpdateStatusParcelRequest(1L);
        // when
        final UpdateStatus updateStatus = parcelStatusControlChangeServicePort.updateStatus(parcelRequest);
        // then
        assertEquals(UpdateStatus.NOT_OK, updateStatus);
    }
}
