package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.ReadModelRebuildPort;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class OperatorReadModelControllerTest {

    private final ReadModelRebuildPort readModelRebuildPort = mock(ReadModelRebuildPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new OperatorReadModelController(readModelRebuildPort))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder.json().build()))
                .build();
    }

    @Test
    void shouldAcceptRebuildRequest() throws Exception {
        final StartReadModelRebuildCommand command = new StartReadModelRebuildCommand("SHIPMENT",
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 17));
        when(readModelRebuildPort.start(OperatorId.of(20L), command)).thenReturn(new ReadModelRebuildJob(
                UUID.fromString("11111111-1111-1111-1111-111111111111"), "SHIPMENT", "PENDING", command.dateFrom(),
                command.dateTo(), OperatorId.of(20L), null, null, null, null));

        mockMvc.perform(post("/operators/20/read-models/SHIPMENT/rebuild")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateFrom\":\"2026-10-01\",\"dateTo\":\"2026-10-17\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.jobId").value("11111111-1111-1111-1111-111111111111"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldRejectRequestWithoutDates() throws Exception {
        mockMvc.perform(post("/operators/20/read-models/SHIPMENT/rebuild")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(readModelRebuildPort);
    }
}
