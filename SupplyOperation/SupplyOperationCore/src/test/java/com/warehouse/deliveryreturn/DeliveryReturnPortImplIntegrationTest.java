package com.warehouse.deliveryreturn;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.domain.vo.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelProperty;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;

@SpringBootTest(classes = DeliveryReturnPortImplIntegrationTest.DeliveryReturnPortIntegrationTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryReturnPortImplIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.deliveryreturn" })
    @EntityScan(basePackages = { "com.warehouse.deliveryreturn" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn" })
    @EnableAutoConfiguration
    public static class DeliveryReturnPortIntegrationTestConfiguration {

        @MockBean
        public ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

        @MockBean
        public RouteLogServicePort routeLogServicePort;

        @MockBean
        public ParcelProperty parcelProperty;

        @MockBean
        public ParcelRepositoryServicePort parcelRepositoryServicePort;

        @MockBean
        public MailServicePort mailServicePort;

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8080").build();
        }
    }

    @Autowired
    private DeliveryReturnPort deliveryReturnPort;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ParcelProperty parcelProperty;

    @Autowired
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

    @Autowired
    private RouteLogServicePort routeLogServicePort;

    @Autowired
    private ParcelRepositoryServicePort parcelRepositoryServicePort;

    @Autowired
    private MailServicePort mailServicePort;

    private final DeviceInformation deviceInformation = new DeviceInformation(
            "1", 1L, "s-soja", "KT1"
    );

    @BeforeEach
    void setup() {
        reset(parcelStatusControlChangeServicePort);
    }
    
    @Test
    void shouldDeliverReturnAndNotUpdateParcelWhenIsNotAllowed() {
        // given
        final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L,
                DeliveryStatus.RETURN, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        final Parcel parcel = Parcel.builder()
                .recipientEmail("recipient")
                .parcelStatus("RETURN")
                .id(1L)
                .senderEmail("sender")
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.NOT_OK);
        when(parcelRepositoryServicePort.downloadParcel(1L)).thenReturn(parcel);

        doNothing()
                .when(mailServicePort)
                .sendNotification(parcel);

        doNothing()
                .when(routeLogServicePort)
                .logDeliverReturn(any());
        // when
        final DeliveryReturnResponse response = deliveryReturnPort.deliverReturn(request);
        // then
        assertThat(response.getDeliveryReturnResponses())
                .extracting(DeliveryReturnResponseDetails::getReturnToken,
                        DeliveryReturnResponseDetails::getDeliveryStatus,
                        DeliveryReturnResponseDetails::getUpdateStatus)
                .containsExactly(Tuple.tuple("12345", "RETURN", UpdateStatus.NOT_OK));

        assertEquals("s-soja", response.getSupplierCode());
        assertEquals("KT1", response.getDepotCode());
    }

    @Test
    void shouldDeliverReturn() {
        // given
        final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L,
                DeliveryStatus.RETURN, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        final Parcel parcel = Parcel.builder()
                .recipientEmail("recipient")
                .parcelStatus("RETURN")
                .id(1L)
                .senderEmail("sender")
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.OK);
        when(parcelRepositoryServicePort.downloadParcel(1L)).thenReturn(parcel);
        doNothing()
                .when(mailServicePort)
                .sendNotification(parcel);
        doNothing()
                .when(routeLogServicePort)
                .logDeliverReturn(any());
        // when
        final DeliveryReturnResponse response = deliveryReturnPort.deliverReturn(request);
        // then
        assertThat(response.getDeliveryReturnResponses())
                .extracting(DeliveryReturnResponseDetails::getReturnToken,
                        DeliveryReturnResponseDetails::getDeliveryStatus,
                        DeliveryReturnResponseDetails::getUpdateStatus)
                .containsExactly(Tuple.tuple("12345", "RETURN", UpdateStatus.OK));

        assertEquals("s-soja", response.getSupplierCode());
        assertEquals("KT1", response.getDepotCode());
    }

    @Test
    void shouldBreakProcessWhenBusinessExceptionIsThrown() {
        // given
        final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L,
                DeliveryStatus.RETURN, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.NOT_OK);
        doThrow(new BusinessException(404, "Parcel 1 was not found"))
                .when(parcelRepositoryServicePort)
                .downloadParcel(1L);
        // when
        final Executable executable = () -> deliveryReturnPort.deliverReturn(request);
        // then
        final BusinessException exception = assertThrows(BusinessException.class, executable);
        assertEquals("Parcel 1 was not found", exception.getMessage());
    }

    @Test
    void shouldBreakProcessWhenTechnicalExceptionIsThrown() {
        // given
        final List<DeliveryReturnDetails> deliveryReturnDetails = buildReturnDetails(1L,
                DeliveryStatus.RETURN, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.OK);
        doThrow(new TechnicalException(500, "Could not establish connection"))
                .when(parcelRepositoryServicePort)
                .downloadParcel(1L);
        // when
        final Executable executable = () -> deliveryReturnPort.deliverReturn(request);
        // then
        final TechnicalException exception = assertThrows(TechnicalException.class, executable);
        assertEquals("Could not establish connection", exception.getMessage());
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
