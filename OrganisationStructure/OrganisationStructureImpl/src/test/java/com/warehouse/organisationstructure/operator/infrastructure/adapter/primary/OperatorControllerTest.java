package com.warehouse.organisationstructure.operator.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OperatorControllerTest {

    private final JsonMapper objectMapper = JsonMapper.builder().configureForJackson2()
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS).build();

    private OperatorPort operatorPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.operatorPort = mock(OperatorPort.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new OperatorController(this.operatorPort))
                .setMessageConverters(new JacksonJsonHttpMessageConverter(this.objectMapper))
                .build();
    }

    @Test
    void shouldReturn400WhenCreatingOperatorWithNonPositivePaymentDeadline() throws Exception {
        this.mockMvc.perform(post("/operators").contentType(MediaType.APPLICATION_JSON)
                        .content(withPaymentDeadline(this.objectMapper.writeValueAsString(
                                OperatorTestFixtures.createApiRequest()), 0)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.operatorPort);
    }

    @Test
    void shouldReturn400WhenUpdatingOperatorWithNonPositivePaymentDeadline() throws Exception {
        this.mockMvc.perform(put("/operators/10001").contentType(MediaType.APPLICATION_JSON)
                        .content(withPaymentDeadline(this.objectMapper.writeValueAsString(
                                OperatorTestFixtures.updateApiRequest()), -1)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.operatorPort);
    }

    private String withPaymentDeadline(final String json, final int hours) {
        assertThat(json).contains("\"paymentDeadlineHours\":72");
        return json.replace("\"paymentDeadlineHours\":72", "\"paymentDeadlineHours\":" + hours);
    }
}
