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

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreturn.domain.exception.DeliveryRequestException;
import com.warehouse.deliveryreturn.domain.exception.DeliveryReturnDetailsException;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;
import com.warehouse.deliveryreturn.domain.exception.WrongProcessTypeException;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.terminal.DeviceInformation;

@ExtendWith(MockitoExtension.class)
public class DeliveryReturnPortImplTest {

	@Mock
	private ShipmentStatusControlServicePort shipmentStatusControlServicePort;

	@Mock
	private DeliveryReturnRepository deliveryReturnRepository;

	@Mock
	private ReturnTokenServicePort returnTokenServicePort;

	@Mock
	private ShipmentRepositoryServicePort shipmentRepositoryServicePort;

	@Mock
	private MailServicePort mailServicePort;

	private DeliveryReturnPortImpl returnPort;

	private final DeviceInformation deviceInformation = DeviceInformation.builder()
			.version("1")
			.deviceId(new DeviceId(1L))
			.username("s-soja")
			.departmentCode(new DepartmentCode("KT1"))
			.build();

	@BeforeEach
	void setup() {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
				returnTokenServicePort, shipmentRepositoryServicePort, mailServicePort);
		returnPort = new DeliveryReturnPortImpl(deliveryReturnService, shipmentStatusControlServicePort);
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
				.deliveryStatus(deliveryStatus)
				.shipmentId(new ShipmentId(parcelId))
				.supplierCode(new SupplierCode(supplierCode))
				.departmentCode(new DepartmentCode(depotCode))
				.build();

		return List.of(deliveryReturnDetails);
	}
}
