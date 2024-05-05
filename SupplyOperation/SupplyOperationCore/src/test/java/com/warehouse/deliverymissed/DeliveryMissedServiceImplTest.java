package com.warehouse.deliverymissed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import com.warehouse.deliverymissed.domain.port.secondary.ParcelStatusServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.deliverymissed.domain.enumeration.DeliveryStatus;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedServiceImpl;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;

@ExtendWith(MockitoExtension.class)
public class DeliveryMissedServiceImplTest {
    
    @Mock
    private DeliveryMissedRepository deliveryMissedRepository;

    @Mock
    private ParcelStatusServicePort parcelStatusServicePort;

	private DeliveryMissedServiceImpl deliveryMissedService;

    @BeforeEach
    void setup() {
        deliveryMissedService = new DeliveryMissedServiceImpl(deliveryMissedRepository, parcelStatusServicePort);
    }

    @Test
    void shouldSaveDelivery() {
        // given
        final DeliveryMissedRequest request = createDeliveryMissedRequest(DeliveryStatus.UNAVAILABLE,
                "KT1", 1L, "abc");
		final DeliveryMissed expectedDeliveryMissed = new DeliveryMissed("deliveryId", 1L, "KT1", "abc",
                DeliveryStatus.UNAVAILABLE);
        doReturn(expectedDeliveryMissed)
                .when(deliveryMissedRepository)
                .saveDeliveryMissed(request);
        // when
        final DeliveryMissed deliveryMissed = deliveryMissedService.saveDelivery(request);
        // then
        assertEquals(expectedDeliveryMissed, deliveryMissed);
    }

	private DeliveryMissedRequest createDeliveryMissedRequest(final DeliveryStatus deliveryStatus,
			final String depotCode, final Long parcelId, final String supplierCode) {
		final DeliveryMissedRequest request = new DeliveryMissedRequest();
		request.setDeliveryStatus(deliveryStatus);
		request.setDepotCode(depotCode);
		request.setParcelId(parcelId);
		request.setSupplierCode(supplierCode);
		return request;
	}
}
