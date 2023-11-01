package com.warehouse.deliverytoken;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.TechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.deliverytoken.configuration.DeliveryTokenTestConfiguration;
import com.warehouse.deliverytoken.domain.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.port.primary.DeliveryTokenPort;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.CommunicationException;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.SupplierNotAllowedException;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.property.ShipmentProperty;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTokenTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryTokenIntegrationTest {

    @Autowired
    private DeliveryTokenPort deliveryTokenPort;

    @Autowired
    private ParcelServicePort parcelServicePort;

    @Autowired
    private DeliveryTokenServicePort deliveryTokenServicePort;

    @Autowired
    private ShipmentProperty shipmentProperty;

    @BeforeEach
    void setup() {
        reset(parcelServicePort);
    }

    @Test
    void shouldProtectDelivery() {
        // given
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("dwvscq")
        );
        final Parcel parcel = new Parcel(1L, null, ParcelType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final DeliveryTokenResponse response = deliveryTokenPort.protect(request);
        // then
        final String token = response.getPackageResponses().stream()
                        .map(DeliveryPackageResponse::getProtectedDelivery)
                        .map(ProtectedDelivery::getProtectionToken)
                        .collect(Collectors.joining());
        assertEquals("abcdefghjklk", token);
    }

    @Test
    void shouldThrowCommunicationException() {
        // given
        final String exceptionMessage = "Connection with %s could not be established";
        final String domainName = "Shipment";
        final int code = 8001;
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("abc")
        );
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenThrow(new CommunicationException(domainName));
        // when
        final Executable executable = () -> deliveryTokenPort.protect(request);
        // then
        final CommunicationException exception = assertThrows(CommunicationException.class, executable);
        assertEquals(String.format(exceptionMessage, domainName), exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    void shouldThrowTechnicalException() {
        // given
        final String exceptionMessage = "Http exception while connecting with %s service";
        final String domainName = "Shipment";
        final int code = 8002;
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("abc")
        );
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenThrow(new TechnicalException(domainName));
        // when
        final Executable executable = () -> deliveryTokenPort.protect(request);
        // then
        final TechnicalException exception = assertThrows(TechnicalException.class, executable);
        assertEquals(String.format(exceptionMessage, domainName), exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    void shouldThrowExceptionWhenSupplierIsNotAllowed() {
        // given
        final String exceptionMessage = "Supplier %s does not have any access to deliveries";
        final String supplierCode = "abc";
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier(supplierCode)
        );
        final Parcel parcel = new Parcel(1L, null, ParcelType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final Executable executable = () -> deliveryTokenPort.protect(request);
        // then
        final SupplierNotAllowedException exception = assertThrows(SupplierNotAllowedException.class, executable);
        assertEquals(String.format(exceptionMessage, supplierCode), exception.getMessage());
    }

	private List<DeliveryPackageRequest> createDeliveryRequests(Long parcelId, Long parcelRelatedId,
			ParcelType parcelType, String destination, String deliveryId) {
		return Collections.singletonList(
				new DeliveryPackageRequest(createParcel(parcelId, parcelRelatedId, parcelType, destination),
						 createDelivery(deliveryId)));
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
}
