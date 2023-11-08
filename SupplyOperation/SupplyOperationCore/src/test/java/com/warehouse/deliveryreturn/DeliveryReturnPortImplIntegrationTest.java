package com.warehouse.deliveryreturn;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;

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
    private ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort;

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
                DeliveryStatus.DELIVERY, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.NOT_OK);
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
                DeliveryStatus.DELIVERY, "KT1", "abc", null);
        final DeliveryReturnRequest request = buildDeliveryReturnRequest(
                ProcessType.RETURN, deviceInformation, deliveryReturnDetails);

        final UpdateStatusParcelRequest updateStatusParcelRequest = UpdateStatusParcelRequest.builder()
                .parcelId(1L)
                .build();

        when(parcelStatusControlChangeServicePort.updateStatus(updateStatusParcelRequest))
                .thenReturn(UpdateStatus.OK);
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
