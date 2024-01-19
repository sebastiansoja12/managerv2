package com.warehouse.deliveryreturn;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.warehouse.tools.supplier.SupplierValidatorProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.deliveryreturn.configuration.DeliveryReturnTestConfiguration;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteDetails;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;

@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Disabled
public class RouteLogServiceAdapterTest {

    @Mock
    private RestClient restClient;

    @Autowired
    private RouteLogServicePort routeLogServicePort;

    @Autowired
    private SupplierValidatorProperties supplierValidatorProperties;

    @Test
    void shouldLogDeliverReturn() {
        // given
        final DeliveryReturnRouteRequest request = DeliveryReturnRouteRequest.builder()
                .deliveryReturnRouteDetails(List.of(DeliveryReturnRouteDetails.builder()
                        .parcelId(1L)
                        .build()))
                .build();
        // when
        routeLogServicePort.logDeliverReturn(request);
        // then
        verify(restClient, times(1)).post();
    }
}
