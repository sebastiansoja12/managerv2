package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.OperatorDataPort;
import com.warehouse.platformadministration.application.port.primary.OperatorDevicePort;
import com.warehouse.platformadministration.application.port.primary.OperatorOnboardingPort;
import com.warehouse.platformadministration.application.port.primary.command.EraseOperatorDataCommand;
import com.warehouse.platformadministration.domain.exception.OperatorDataErasureNotAllowedException;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.domain.model.OperatorDataExport;
import com.warehouse.platformadministration.domain.model.OperatorOnboarding;
import com.warehouse.platformadministration.domain.vo.OnboardingStep;
import com.warehouse.platformadministration.domain.vo.OnboardingStepType;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class OperatorOperationsControllerTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final OperatorOnboardingPort operatorOnboardingPort = mock(OperatorOnboardingPort.class);

    private final OperatorDevicePort operatorDevicePort = mock(OperatorDevicePort.class);

    private final OperatorDataPort operatorDataPort = mock(OperatorDataPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new OperatorOperationsController(operatorOnboardingPort, operatorDevicePort,
                operatorDataPort))
                .setMessageConverters(new JacksonJsonHttpMessageConverter())
                .build();
    }

    @Test
    void shouldReturnOnboardingProgress() throws Exception {
        when(operatorOnboardingPort.find(OPERATOR_ID)).thenReturn(new OperatorOnboarding(OPERATOR_ID, List.of(
                new OnboardingStep(OnboardingStepType.DEPARTMENT, true, 2),
                new OnboardingStep(OnboardingStepType.TEST_SHIPMENT, false, 0))));

        mockMvc.perform(get("/operators/20/onboarding"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedSteps").value(1))
                .andExpect(jsonPath("$.totalSteps").value(2))
                .andExpect(jsonPath("$.steps[0].type").value("DEPARTMENT"))
                .andExpect(jsonPath("$.steps[0].count").value(2));
    }

    @Test
    void shouldDownloadOperatorExportAsAttachment() throws Exception {
        when(operatorDataPort.export(OPERATOR_ID)).thenReturn(new OperatorDataExport(NOW, "owner",
                new OperatorProfile(OPERATOR_ID, "Acme", "123", null, null, null, null, null, "INACTIVE", NOW),
                List.of(), List.of(), List.of(), List.of(), List.of(), new OperatorShipmentSettings(Map.of("a", 1)),
                List.of()));

        mockMvc.perform(get("/operators/20/data-export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("operator-20-export-")))
                .andExpect(jsonPath("$.format").value("managerv2.operator-export.v1"))
                .andExpect(jsonPath("$.operator.companyName").value("Acme"))
                .andExpect(jsonPath("$.shipmentSettings.a").value(1));
    }

    @Test
    void shouldReportWhyErasureIsNotAllowed() throws Exception {
        when(operatorDataPort.erase(OPERATOR_ID, new EraseOperatorDataCommand("Acme")))
                .thenThrow(new OperatorDataErasureNotAllowedException(OperatorDataErasure.OPERATOR_ACTIVE, "blocked"));

        mockMvc.perform(post("/operators/20/data-erasure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirmation\":\"Acme\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.reason").value("OPERATOR_ACTIVE"));
    }

    @Test
    void shouldReturnNoContentWithoutPreviousErasure() throws Exception {
        when(operatorDataPort.findLastErasure(OPERATOR_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/operators/20/data-erasure"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectInvalidDeviceStatus() throws Exception {
        org.mockito.Mockito.doThrow(new IllegalArgumentException("status")).when(operatorDevicePort)
                .changeDeviceStatus(OPERATOR_ID, "T-1", "LOST");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/operators/20/devices/T-1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"LOST\"}"))
                .andExpect(status().isBadRequest());
    }
}
