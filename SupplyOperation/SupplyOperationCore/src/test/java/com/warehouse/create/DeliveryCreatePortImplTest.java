package com.warehouse.create;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.create.domain.enumeration.ProcessType;
import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.domain.model.DeviceInformation;
import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.model.Response;
import com.warehouse.create.domain.port.primary.DeliveryCreatePortImpl;
import com.warehouse.create.domain.port.secondary.DeliveryCreateRepository;
import com.warehouse.create.domain.port.secondary.RouteLogServicePort;
import com.warehouse.create.domain.service.DeliveryCreateService;
import com.warehouse.create.domain.service.DeliveryCreateServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryCreatePortImplTest {
    

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private DeliveryCreateRepository deliveryCreateRepository;
    
    private DeliveryCreatePortImpl deliveryCreatePort;

    private final DeviceInformation deviceInformation = new DeviceInformation("1.0", 1L,
            "s-soja", "KT1");
    
    
    @BeforeEach
    void setup() {
		final DeliveryCreateService deliveryCreateService = new DeliveryCreateServiceImpl(routeLogServicePort,
				deliveryCreateRepository);
        deliveryCreatePort = new DeliveryCreatePortImpl(deliveryCreateService);
    }


    @Test
    void shouldCreateDelivery() {
        // given
        final Request request = Request.builder()
                .processType(ProcessType.CREATED)
                .deviceInformation(deviceInformation)
                .parcelId(1L)
                .build();

        final DeliveryCreate deliveryCreate = DeliveryCreate.builder()
                .parcelId(1L)
                .processId("8644a22c-fd67-4421-a33b-616ca2b360b6")
                .terminalId(1L)
                .supplierCode("s-soja")
                .version("1.0")
                .build();

        doReturn(deliveryCreate)
                .when(deliveryCreateRepository)
                .save(request);

        // when
        final Response response = deliveryCreatePort.createDelivery(request);

        // then
        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionWhenProcessTypeIsNotCreated() {
        // given
        final Request request = Request.builder()
                .processType(ProcessType.RETURN)
                .deviceInformation(deviceInformation)
                .parcelId(1L)
                .build();
        // when
        final Executable executable = () -> deliveryCreatePort.createDelivery(request);
        // then
        assertThrows(IllegalArgumentException.class, executable);
    }
}
