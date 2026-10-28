package com.warehouse.csv;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

@SpringBootTest(classes = CsvControllerIntegrationTest.CsvControllerTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CsvControllerIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.csv" })
    @EnableAutoConfiguration
    public static class CsvControllerTestConfiguration {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShipmentApiService shipmentApiService;

    @Test
    void shouldGenerateCsv() throws Exception {
        // given
        final Long parcelId = 1L;
        final ShipmentPartyDto party = new ShipmentPartyDto("Jan", "Kowalski", "jan@test.pl", "123456789",
                "Katowice", "40-000", "Mariacka 1", "PL");
        when(shipmentApiService.getShipmentDocument(any())).thenReturn(new ShipmentDocumentDto(
                new ShipmentId(parcelId), null, null, null, null, party, party));
        // when
        mockMvc.perform(get("/v1/api/csv/{id}", parcelId).contextPath("/v1/api"))
        // then
                .andExpect(status().isOk());
    }
}
