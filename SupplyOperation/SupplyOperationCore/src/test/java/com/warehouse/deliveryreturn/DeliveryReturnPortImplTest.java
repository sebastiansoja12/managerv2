package com.warehouse.deliveryreturn;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryReturnPortImplTest {

    @Mock
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private DeliveryReturnRepository deliveryReturnRepository;

    @Mock
    private DeliveryReturnTokenServicePort deliveryReturnTokenServicePort;

    private DeliveryReturnPortImpl returnPort;

    private final DeviceInformation deviceInformation = new DeviceInformation(
            "1", 1L, "s-soja", "KT1"
    );


    @BeforeEach
    void setup() {
        final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
                deliveryReturnTokenServicePort);
        returnPort = new DeliveryReturnPortImpl(deliveryReturnService, parcelStatusControlChangeServicePort,
                routeLogServicePort);
    }

    @Test
    void shouldDeliverReturn() {
        // given
        final DeliveryReturnRequest deliveryReturnRequest = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, 1L, DeliveryStatus.DELIVERY, "KT1",
                "abc", "token"
        );
        // when

        // then
    }

	private DeliveryReturnRequest buildDeliveryReturnRequest(ProcessType processType,
			DeviceInformation deviceInformation, Long parcelId, DeliveryStatus deliveryStatus, String depotCode,
			String supplierCode, String token) {
		final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(parcelId, deliveryStatus,
				depotCode, supplierCode, token);
		return new DeliveryReturnRequest(processType, deviceInformation, deliveryReturnDetails);
	}
    
	private List<DeliveryReturnDetails> buildReturnDetails(Long parcelId, DeliveryStatus deliveryStatus,
			String depotCode, String supplierCode, String token) {
        
        final DeliveryReturnDetails deliveryReturnDetails = DeliveryReturnDetails.builder()
                .deliveryStatus(deliveryStatus)
                .parcelId(parcelId)
                .supplierCode(supplierCode)
                .depotCode(depotCode)
                .token(token)
                .build();
		
		return List.of(deliveryReturnDetails);
	}
}
