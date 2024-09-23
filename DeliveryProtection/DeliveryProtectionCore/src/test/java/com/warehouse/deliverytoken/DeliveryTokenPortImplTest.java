package com.warehouse.deliverytoken;


import com.warehouse.commonassets.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.exception.MissingParcelIdException;
import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.port.primary.DeliveryTokenPortImpl;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.domain.service.DeliveryService;
import com.warehouse.deliverytoken.domain.service.DeliveryServiceImpl;
import com.warehouse.deliverytoken.domain.vo.*;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.CommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class DeliveryTokenPortImplTest {
    
    @Mock
    private DeliveryTokenServicePort deliveryTokenServicePort;
    
    @Mock
    private ParcelServicePort parcelServicePort;
    
    private DeliveryTokenPortImpl deliveryTokenPort;
    
    @BeforeEach
    void setup() {
        final DeliveryService service = new DeliveryServiceImpl(deliveryTokenServicePort);
        deliveryTokenPort = new DeliveryTokenPortImpl(service, parcelServicePort);
    }
    
    @Test
    void shouldProtectTheDelivery() {
		// given
		final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("abc")
        );

		final List<DeliveryPackageResponse> deliveryPackageResponses = createDeliveryPackageResponses(
                1L, null, ParcelType.PARENT, "KT1", "1", "1"
        );

		final DeliveryTokenResponse expectedResponse = new DeliveryTokenResponse(deliveryPackageResponses, "abc");

		final ParcelId parcelId = new ParcelId(1L);

		final Parcel parcel = createParcel(1L, null, ParcelType.PARENT, "KT1");

		doReturn(parcel)
                .when(parcelServicePort)
                .downloadParcel(parcelId);

		doReturn(expectedResponse)
                .when(deliveryTokenServicePort)
                .protect(request);
		// when
		final DeliveryTokenResponse response = deliveryTokenPort.protect(request);
		// then
		assertEquals(expectedResponse.getPackageResponses(), response.getPackageResponses());
    }

    @Test
    void shouldFailWhenParcelIdIsMissing() {
        // given
        final String exceptionMessage = "Missing parcel in request";
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(null, null, null, null, "1"),
                new Supplier("abc")
        );
        // when
        final Executable executable = () -> deliveryTokenPort.protect(request);
        // then
        final MissingParcelIdException exception = assertThrows(MissingParcelIdException.class, executable);
        assertEquals(exceptionMessage, exception.getMessage());
    }
    
    @Test
    void shouldThrowCommunicationException() {
		// given
        final String exceptionMessage = "Connection with %s could not be established";
        final int code = 400;
        final String domainName = "Shipment";
		final DeliveryTokenRequest request = new DeliveryTokenRequest(
				createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("abc"));

        final ParcelId parcelId = new ParcelId(1L);

		doThrow(new CommunicationException(domainName))
                .when(parcelServicePort)
                .downloadParcel(parcelId);
        // when
        final Executable executable = () -> deliveryTokenPort.protect(request);
        // then
        final CommunicationException exception = assertThrows(CommunicationException.class, executable);
        assertEquals(String.format(exceptionMessage, domainName), exception.getMessage());
        assertEquals(code, exception.getCode());

    }

    private List<DeliveryPackageResponse> createDeliveryPackageResponses(Long parcelId, Long parcelRelatedId,
			ParcelType parcelType, String destination, String supplierTokenServiceApplicationId,
			String deliveryId) {
		return Collections.singletonList(
				new DeliveryPackageResponse(createParcel(parcelId, parcelRelatedId, parcelType, destination),
                        supplierTokenServiceApplicationId, createProtectedDelivery(deliveryId)));
	}

	private List<DeliveryPackageRequest> createDeliveryRequests(Long parcelId, Long parcelRelatedId,
                                                                ParcelType parcelType, String destination, String deliveryId) {
        return Collections
                .singletonList(new DeliveryPackageRequest(
                        createParcel(parcelId, parcelRelatedId, parcelType, destination),
                        createDelivery(deliveryId))
                );
    }

    private Delivery createDelivery(String id) {
        return new Delivery(id);
    }

    private Supplier createSupplier(String supplierCode) {
        return new Supplier(supplierCode);
    }

    private Parcel createParcel(Long id, Long parcelRelatedId, ParcelType parcelType, String destination) {
        return new Parcel(id, parcelRelatedId, parcelType, destination);
    }

    private ProtectedDelivery createProtectedDelivery(String deliveryId) {
        return new ProtectedDelivery("protectionToken", deliveryId);
    }
}
