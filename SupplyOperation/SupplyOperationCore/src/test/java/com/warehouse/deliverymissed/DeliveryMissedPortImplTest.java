package com.warehouse.deliverymissed;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.deliverymissed.domain.enumeration.DeliveryStatus;
import com.warehouse.deliverymissed.domain.exception.EmptyDepotCodeException;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.primary.DeliveryMissedPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.port.secondary.ParcelStatusServicePort;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

@ExtendWith(MockitoExtension.class)
public class DeliveryMissedPortImplTest {


    @Mock
    private RouteLogMissedServicePort routeLogMissedServicePort;

    @Mock
    private DeliveryMissedRepository deliveryMissedRepository;

    @Mock
    private ParcelStatusServicePort parcelStatusServicePort;

    private DeliveryMissedService deliveryMissedService;

    private DeliveryMissedPortImpl deliveryMissedPort;


    @BeforeEach
    void setup() {
        deliveryMissedService = new DeliveryMissedServiceImpl(deliveryMissedRepository, parcelStatusServicePort);
        deliveryMissedPort = new DeliveryMissedPortImpl(deliveryMissedService, routeLogMissedServicePort);
    }

    @Test
    void shouldLogMissedDelivery() {
        // given
        final DeliveryMissedRequest request = createDeliveryMissedRequest(
                "KT1", 1L, "abc");
        final DeliveryMissed deliveryMissed = new DeliveryMissed("deliveryId", 1L, "KT1", "abc", DeliveryStatus.UNAVAILABLE);
        when(deliveryMissedRepository
                .saveDeliveryMissed(request))
                .thenReturn(deliveryMissed);
        doNothing()
                .when(routeLogMissedServicePort)
                .logRouteLogMissedDelivery(deliveryMissed);
        doNothing()
                .when(routeLogMissedServicePort)
                .logDepotCodeMissedDelivery(deliveryMissed);
        // when
        final DeliveryMissedResponse response = deliveryMissedPort.logMissedDelivery(request);
        // then
        assertEquals(response.getDeliveryId(), deliveryMissed.getDeliveryId());
    }

    @Test
    void shouldNotLogMissedDeliveryWhenDepotCodeIsEmpty() {
        // given
        final DeliveryMissedRequest request = createDeliveryMissedRequest(
                "", 1L, "abc");
        // when
        final Executable executable = () -> deliveryMissedPort.logMissedDelivery(request);
        // then
        final EmptyDepotCodeException runtimeException = assertThrows(EmptyDepotCodeException.class, executable);
        assertEquals("Depot code cannot be empty", runtimeException.getMessage());
    }

	private DeliveryMissedRequest createDeliveryMissedRequest(final String depotCode, final Long parcelId,
			final String supplierCode) {
		final DeliveryMissedRequest request = new DeliveryMissedRequest();
		request.setDepotCode(depotCode);
		request.setParcelId(parcelId);
		request.setSupplierCode(supplierCode);
		return request;
	}
}
