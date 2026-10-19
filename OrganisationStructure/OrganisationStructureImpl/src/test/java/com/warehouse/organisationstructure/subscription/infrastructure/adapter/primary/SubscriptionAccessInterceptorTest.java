package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warehouse.organisationstructure.subscription.InMemoryOperatorSubscriptionStore;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPortImpl;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Set;

import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.IMPERSONATING_PLATFORM_OWNER;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.NOW;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.OPERATOR_ID;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.STARTER_SUBSCRIPTION;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.TODAY;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.clockAt;
import static com.warehouse.organisationstructure.subscription.OperatorSubscriptionFixture.subscription;
import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionAccessInterceptorTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private InMemoryOperatorSubscriptionStore store;

    private SubscriptionAccessInterceptor interceptor;

    @BeforeEach
    void setUp() {
        store = new InMemoryOperatorSubscriptionStore(Set.of(OPERATOR_ID));
        store.actAsOperator(OPERATOR_ID);
        interceptor = new SubscriptionAccessInterceptor(
                new OperatorSubscriptionPortImpl(store, store, store, store, STARTER_SUBSCRIPTION, clockAt(NOW)), objectMapper);
    }

    @Test
    void shouldBlockOperatorRequestWhenSubscriptionExpired() throws Exception {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean proceed = interceptor.preHandle(new MockHttpServletRequest("POST", "/shipments"), response,
                new Object());

        final JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(proceed).isFalse();
        assertThat(response.getStatus()).isEqualTo(402);
        assertThat(body.get("subscriptionInactive").asBoolean()).isTrue();
        assertThat(body.get("status").asText()).isEqualTo("EXPIRED");
        assertThat(body.get("plan").asText()).isEqualTo("BASIC");
        assertThat(body.get("endDate").asText()).isEqualTo(TODAY.minusDays(1).toString());
    }

    @Test
    void shouldBlockOperatorRequestWhenSubscriptionHasNotStarted() throws Exception {
        store.save(subscription(SubscriptionPlan.MINI, TODAY.plusDays(1), TODAY.plusMonths(1)));
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean proceed = interceptor.preHandle(new MockHttpServletRequest("GET", "/departments"), response,
                new Object());

        assertThat(proceed).isFalse();
        assertThat(objectMapper.readTree(response.getContentAsString()).get("status").asText())
                .isEqualTo("NOT_STARTED");
    }

    @Test
    void shouldBlockOperatorRequestWhenNoPlanIsAssigned() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean proceed = interceptor.preHandle(new MockHttpServletRequest("GET", "/shipments"), response,
                new Object());

        assertThat(proceed).isFalse();
        assertThat(response.getStatus()).isEqualTo(402);
        assertThat(objectMapper.readTree(response.getContentAsString()).get("status").asText())
                .isEqualTo("NOT_ASSIGNED");
    }

    @Test
    void shouldLetOperatorRequestThroughWhenSubscriptionActive() throws Exception {
        store.save(subscription(SubscriptionPlan.ENTERPRISE, TODAY, TODAY));

        assertThat(interceptor.preHandle(new MockHttpServletRequest("POST", "/shipments"),
                new MockHttpServletResponse(), new Object())).isTrue();
    }

    @Test
    void shouldKeepAuthenticationAndSubscriptionStatusAvailable() throws Exception {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));

        assertThat(preHandle("POST", "/auth/login")).isTrue();
        assertThat(preHandle("POST", "/auth/logout")).isTrue();
        assertThat(preHandle("GET", "/auth/me")).isTrue();
        assertThat(preHandle("GET", "/subscription")).isTrue();
        assertThat(preHandle("GET", "/maintenance")).isTrue();
        assertThat(preHandle("OPTIONS", "/shipments")).isTrue();
    }

    @Test
    void shouldKeepPlatformPathsAvailableWhenOperatorUserHasNoPlan() throws Exception {
        assertThat(preHandle("POST", "/platform/auth/login")).isTrue();
        assertThat(preHandle("POST", "/platform/auth/refresh")).isTrue();
        assertThat(preHandle("GET", "/platform/auth/me")).isTrue();
        assertThat(preHandle("GET", "/platform/announcements")).isTrue();
    }

    @Test
    void shouldNotTreatSimilarPathAsAvailablePath() throws Exception {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));

        assertThat(preHandle("GET", "/platforms")).isFalse();
        assertThat(preHandle("GET", "/subscriptions")).isFalse();
        assertThat(preHandle("GET", "/authors")).isFalse();
        assertThat(preHandle("POST", "/auth/me/api-key")).isFalse();
        assertThat(preHandle("PUT", "/auth/me/fullnames")).isFalse();
    }

    @Test
    void shouldLetImpersonatingPlatformOwnerThrough() throws Exception {
        store.save(subscription(SubscriptionPlan.BASIC, TODAY.minusMonths(1), TODAY.minusDays(1)));
        store.actAsPlatformActor(IMPERSONATING_PLATFORM_OWNER);

        assertThat(preHandle("GET", "/shipments")).isTrue();
    }

    private boolean preHandle(final String method, final String path) throws Exception {
        return interceptor.preHandle(new MockHttpServletRequest(method, path), new MockHttpServletResponse(),
                new Object());
    }
}
