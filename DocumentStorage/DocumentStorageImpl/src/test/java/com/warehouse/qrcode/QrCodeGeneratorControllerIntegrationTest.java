package com.warehouse.qrcode;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.qrcode.domain.port.secondary.DeliveryServicePort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

@SpringBootTest(classes = QrCodeGeneratorControllerIntegrationTest.QrCodeControllerTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class QrCodeGeneratorControllerIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.qrcode" })
    @EnableAutoConfiguration
    public static class QrCodeControllerTestConfiguration {

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8080").build();
        }
    }

    @Autowired
    private RestClient restClient;

    @MockBean
    private ShipmentApiService shipmentApiService;

    @MockBean
    private DeliveryServicePort deliveryServicePort;

    @Test
    void shouldGenerateQrCodeForParcel() {
        // given
        final Long parcelId = 1L;
        final ShipmentPartyDto party = new ShipmentPartyDto("Jan", "Kowalski", "jan@test.pl", "123456789",
                "Katowice", "40-000", "Mariacka 1", "PL");
        when(shipmentApiService.getShipmentDocument(any())).thenReturn(new ShipmentDocumentDto(
                new ShipmentId(parcelId), null, null, "INP000000001", null, party, party));
        // when
        final ResponseEntity<Void> qrCode = restClient.get()
                .uri("/v1/api/qrcodes/{id}", parcelId)
                .retrieve()
                .toBodilessEntity();
        // then
        assertThat(qrCode.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
}
