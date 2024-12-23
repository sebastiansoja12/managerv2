package com.warehouse.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPortImpl;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.delivery.domain.port.secondary.RouteLogDeliveryStatusServicePort;
import com.warehouse.delivery.domain.service.DeliveryService;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;
import com.warehouse.delivery.domain.vo.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryPortImplTest {

	@Mock
	private RouteLogDeliveryStatusServicePort logServicePort;

	@Mock
	private DeliveryRepository deliveryRepository;

	@Mock
	private DeliveryTokenServicePort deliveryTokenServicePort;

	private DeliveryPortImpl deliveryPort;

	private DeliveryService deliveryService;

	private static final String DELIVERY_ID = "053e792f-6201-4365-a87a-f16e7f34b978";

	@BeforeEach
	void setup() {
		deliveryService = new DeliveryServiceImpl(deliveryRepository, deliveryTokenServicePort);
		deliveryPort = new DeliveryPortImpl(deliveryService, logServicePort);
	}

	@Test
	void shouldProcessDelivery() {
		// given
		final String supplierCode = "abc";
		final DeliveryRequest deliveryRequestSet = null;
		final List<DeliveryPackageRequest> deliveryPackageRequests = createDeliveryPackageRequests("KT1",
				DeliveryStatus.DELIVERY, 1L);
		final Supplier supplier = createSupplier(supplierCode);
		final DeliveryTokenRequest deliveryTokenRequest = DeliveryTokenRequest.builder()
				.deliveryPackageRequests(deliveryPackageRequests).supplier(supplier).build();

		final DeliveryTokenResponse deliveryTokenResponse = createDeliveryTokenResponse(supplierCode,
				List.of(buildSupplierSignature("123", 1L)));

		final Delivery updatedDelivery = createDelivery();
		updatedDelivery.setId(UUID.fromString(DELIVERY_ID));

		when(deliveryTokenServicePort.protect(deliveryTokenRequest)).thenReturn(deliveryTokenResponse);

		// when
		final Set<DeliveryResponse> deliveries = deliveryPort.processDelivery(Set.of(deliveryRequestSet));
		// then
		assertThat(deliveries).size().isEqualTo(1);
		final UUID id = null;
		assertEquals(expectedToBe(UUID.fromString(DELIVERY_ID)), id);
	}

	private SupplierSignature buildSupplierSignature(final String token, final Long parcelId) {
		return SupplierSignature.builder().deliveryId(UUID.fromString(DELIVERY_ID)).token(token)
				.build();
	}

	private DeliveryTokenResponse createDeliveryTokenResponse(final String supplierCode,
			final List<SupplierSignature> supplierSignatures) {
		return DeliveryTokenResponse.builder().supplierCode(supplierCode).supplierSignature(supplierSignatures).build();
	}

	private Supplier createSupplier(final String supplierCode) {
		return new Supplier(supplierCode);
	}

	private List<DeliveryPackageRequest> createDeliveryPackageRequests(final String depotCode,
			final DeliveryStatus deliveryStatus, final Long parcelId) {
		return List.of(DeliveryPackageRequest.builder().delivery(DeliveryInformation.builder().parcelId(parcelId)
				.deliveryStatus(deliveryStatus).depotCode(depotCode).build()).build());
	}

	private Delivery createDelivery() {
		return Delivery.builder().deliveryStatus(DeliveryStatus.DELIVERY).depotCode("KT1").supplierCode("abc")
				.parcelId(1L).build();
	}

	private <T> T expectedToBe(T t) {
		return t;
	}
}
