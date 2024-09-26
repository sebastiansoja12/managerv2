package com.warehouse.deliveryreturn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.exception.DeliveryRequestException;
import com.warehouse.deliveryreturn.domain.exception.DeliveryReturnDetailsException;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;
import com.warehouse.deliveryreturn.domain.exception.WrongProcessTypeException;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryReturnPortImplTest {

	@Mock
	private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

	@Mock
	private DeliveryReturnRepository deliveryReturnRepository;

	@Mock
	private DeliveryReturnTokenServicePort deliveryReturnTokenServicePort;

	@Mock
	private ParcelRepositoryServicePort parcelRepositoryServicePort;

	@Mock
	private MailServicePort mailServicePort;

	@Mock
	private RouteLogReturnServicePort routeLogReturnServicePort;

	private DeliveryReturnPortImpl returnPort;

	private final DeviceInformation deviceInformation = new DeviceInformation("1", 1L, "s-soja", "KT1");

	@BeforeEach
	void setup() {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
				deliveryReturnTokenServicePort, parcelRepositoryServicePort, mailServicePort);
		returnPort = new DeliveryReturnPortImpl(deliveryReturnService, parcelStatusControlChangeServicePort,
				routeLogReturnServicePort);
	}

	@Test
	void shouldThrowEmptyDeliveryReturnRequestException() {
		// given
		final String message = "Delivery return request cannot be null";
		// when
		final Executable executable = () -> returnPort.deliverReturn(null);
		// then
		final DeliveryRequestException exception = assertThrows(DeliveryRequestException.class, executable);
		assertEquals(message, exception.getMessage());
	}

	@Test
	void shouldThrowEmptyDeliveryReturnDetailsException() {
		// given
		final String message = "Delivery return details cannot be null";

		final DeliveryReturnRequest deliveryReturnRequest = buildDeliveryReturnRequest(ProcessType.RETURN,
				deviceInformation, null);
		// when
		final Executable executable = () -> returnPort.deliverReturn(deliveryReturnRequest);
		// then
		final DeliveryReturnDetailsException exception = assertThrows(DeliveryReturnDetailsException.class, executable);
		assertEquals(message, exception.getMessage());
	}

	@Test
	void shouldThrowProcessTypeException() {
		// given
		final String message = "Wrong process type";
		final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L, DeliveryStatus.DELIVERY, "KT1",
				"abc", "token");
		final DeliveryReturnRequest deliveryReturnRequest = buildDeliveryReturnRequest(ProcessType.REJECT,
				deviceInformation, deliveryReturnDetails);
		// when
		final Executable executable = () -> returnPort.deliverReturn(deliveryReturnRequest);
		// then
		final WrongProcessTypeException exception = assertThrows(WrongProcessTypeException.class, executable);
		assertEquals(message, exception.getMessage());
	}

	@Test
	void shouldThrowWrongDeliveryException() {
		// given
		final String message = "Wrong delivery status";
		final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L, DeliveryStatus.SENDER, "KT1",
				"abc", "token");
		final DeliveryReturnRequest deliveryReturnRequest = buildDeliveryReturnRequest(ProcessType.RETURN,
				deviceInformation, deliveryReturnDetails);
		// when
		final Executable executable = () -> returnPort.deliverReturn(deliveryReturnRequest);
		// then
		final WrongDeliveryStatusException exception = assertThrows(WrongDeliveryStatusException.class, executable);
		assertEquals(message, exception.getMessage());
	}

	private DeliveryReturnRequest buildDeliveryReturnRequest(ProcessType processType,
			DeviceInformation deviceInformation, List<DeliveryReturnDetails> deliveryReturnDetails) {
		return new DeliveryReturnRequest(processType, deviceInformation, deliveryReturnDetails);
	}

	private List<DeliveryReturnDetails> buildReturnDetails(Long parcelId, DeliveryStatus deliveryStatus,
			String depotCode, String supplierCode, String token) {

		final DeliveryReturnDetails deliveryReturnDetails = DeliveryReturnDetails.builder()
				.deliveryStatus(deliveryStatus).parcelId(parcelId).supplierCode(supplierCode).depotCode(depotCode)
				.token(token).build();

		return List.of(deliveryReturnDetails);
	}
}
