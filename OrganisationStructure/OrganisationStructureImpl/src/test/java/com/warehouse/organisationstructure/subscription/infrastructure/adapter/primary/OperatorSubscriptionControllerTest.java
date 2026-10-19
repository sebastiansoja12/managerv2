package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.organisationstructure.subscription.InMemoryOperatorSubscriptionStore;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPortImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.NOW;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.OPERATOR_ID;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.PLATFORM_OWNER;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.STARTER_SUBSCRIPTION;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.clockAt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class OperatorSubscriptionControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private InMemoryOperatorSubscriptionStore store;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        store = new InMemoryOperatorSubscriptionStore(Set.of(OPERATOR_ID));
        store.actAsPlatformActor(PLATFORM_OWNER);
        final OperatorSubscriptionPortImpl port = new OperatorSubscriptionPortImpl(store, store, store, store,
                STARTER_SUBSCRIPTION, clockAt(NOW));
        mockMvc = MockMvcBuilders.standaloneSetup(new OperatorSubscriptionController(port),
                        new CurrentSubscriptionController(port))
                .setControllerAdvice(new OperatorSubscriptionExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setConversionService(conversionService())
                .build();
    }

    @Test
    void shouldRejectNonPositiveOperatorId() throws Exception {
        assertThat(mockMvc.perform(get("/operators/0/subscription")).andReturn().getResponse().getStatus())
                .isEqualTo(400);
        assertThat(mockMvc.perform(get("/operators/abc/subscription")).andReturn().getResponse().getStatus())
                .isEqualTo(400);
    }

    @Test
    void shouldReturnNoContentWhenOperatorHasNoSubscription() throws Exception {
        final MvcResult result = mockMvc.perform(get("/operators/20/subscription")).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
    }

    @Test
    void shouldAssignAndReadSubscription() throws Exception {
        final JsonNode assigned = json(mockMvc.perform(put("/operators/20/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"enterprise\",\"startDate\":\"2026-10-01\",\"endDate\":\"2026-12-31\"}"))
                .andReturn(), 200);
        final JsonNode loaded = json(mockMvc.perform(get("/operators/20/subscription")).andReturn(), 200);

        assertThat(assigned.get("plan").asText()).isEqualTo("ENTERPRISE");
        assertThat(assigned.get("status").asText()).isEqualTo("ACTIVE");
        assertThat(assigned.get("updatedBy").asText()).isEqualTo("owner");
        assertThat(loaded.get("startDate").asText()).isEqualTo("2026-10-01");
        assertThat(loaded.get("endDate").asText()).isEqualTo("2026-12-31");
        assertThat(loaded.get("operatorId").get("value").asLong()).isEqualTo(20L);
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() throws Exception {
        final MvcResult result = mockMvc.perform(put("/operators/20/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"BASIC\",\"startDate\":\"2026-10-31\",\"endDate\":\"2026-10-01\"}"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(store.find(OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldRejectUnknownPlan() throws Exception {
        final MvcResult result = mockMvc.perform(put("/operators/20/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"PLATINUM\",\"startDate\":\"2026-10-01\",\"endDate\":\"2026-10-31\"}"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    void shouldRejectMissingOrMalformedDates() throws Exception {
        final MvcResult missing = mockMvc.perform(put("/operators/20/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"BASIC\",\"startDate\":\"2026-10-01\"}"))
                .andReturn();
        final MvcResult malformed = mockMvc.perform(put("/operators/20/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"BASIC\",\"startDate\":\"01.10.2026\",\"endDate\":\"2026-10-31\"}"))
                .andReturn();

        assertThat(missing.getResponse().getStatus()).isEqualTo(400);
        assertThat(malformed.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    void shouldReturnNotFoundForUnknownOperator() throws Exception {
        final MvcResult result = mockMvc.perform(put("/operators/404/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plan\":\"BASIC\",\"startDate\":\"2026-10-01\",\"endDate\":\"2026-10-31\"}"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldReturnCurrentOperatorSubscriptionState() throws Exception {
        mockMvc.perform(put("/operators/20/subscription")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plan\":\"MINI\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-09-30\"}"));
        store.actAsPlatformActor(null);
        store.actAsOperator(OPERATOR_ID);

        final JsonNode current = json(mockMvc.perform(get("/subscription")).andReturn(), 200);

        assertThat(current.get("plan").asText()).isEqualTo("MINI");
        assertThat(current.get("status").asText()).isEqualTo("EXPIRED");
        assertThat(current.get("accessAllowed").asBoolean()).isFalse();
    }

    @Test
    void shouldReportNotAssignedSubscriptionForCurrentOperator() throws Exception {
        store.actAsPlatformActor(null);
        store.actAsOperator(OPERATOR_ID);

        final JsonNode current = json(mockMvc.perform(get("/subscription")).andReturn(), 200);

        assertThat(current.get("status").asText()).isEqualTo("NOT_ASSIGNED");
        assertThat(current.get("accessAllowed").asBoolean()).isFalse();
    }

    @Test
    void shouldRestrictSubscriptionManagementToPlatformOwners() {
        final AccessUserControl management = OperatorSubscriptionController.class.getAnnotation(AccessUserControl.class);
        final AccessUserControl current = CurrentSubscriptionController.class.getAnnotation(AccessUserControl.class);

        assertThat(management.audience()).isEqualTo(AccessAudience.PLATFORM_OWNER);
        assertThat(current.audience()).isEqualTo(AccessAudience.ORGANIZATION_USER);
    }

    private JsonNode json(final MvcResult result, final int expectedStatus) throws Exception {
        assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private static FormattingConversionService conversionService() {
        final FormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new OperatorIdPathConverter());
        return conversionService;
    }
}
