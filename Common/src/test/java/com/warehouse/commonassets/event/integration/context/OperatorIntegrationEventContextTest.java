package com.warehouse.commonassets.event.integration.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.StaticListableBeanFactory;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

class OperatorIntegrationEventContextTest {

    private static final OperatorDetails OPERATOR = new OperatorDetails(OperatorId.of(7L), new UserId(11L),
            new DepartmentId(13L));
    private static final IntegrationEvent EVENT = new IntegrationEvent() {
    };

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void shouldContributeOperatorHeadersOfCurrentContext() {
        assertThat(context(Optional.of(OPERATOR)).attributes(EVENT))
                .isEqualTo(Map.of("operatorId", "7", "userId", "11", "departmentId", "13"));
    }

    @Test
    void shouldContributeNothingWithoutOperatorContext() {
        assertThat(context(Optional.empty()).attributes(EVENT)).isEmpty();
    }

    @Test
    void shouldWriteOperatorIdentifiersIntoLegacyPayload() {
        final ObjectNode payload = this.jsonMapper.createObjectNode();

        context(Optional.empty()).enrichLegacyPayload(payload,
                Map.of("operatorId", "7", "userId", "11", "departmentId", "13"));

        assertThat(payload.get("operatorId").get("value").asLong()).isEqualTo(7L);
        assertThat(payload.get("userId").get("value").asLong()).isEqualTo(11L);
        assertThat(payload.get("departmentId").get("value").asLong()).isEqualTo(13L);
    }

    @Test
    void shouldKeepIdentifiersCarriedByEventWhenContextIsMissing() {
        final ObjectNode payload = this.jsonMapper.createObjectNode();
        payload.putObject("operatorId").put("value", 5L);

        context(Optional.empty()).enrichLegacyPayload(payload, Map.of());

        assertThat(payload.get("operatorId").get("value").asLong()).isEqualTo(5L);
        assertThat(payload.get("userId").isNull()).isTrue();
    }

    private OperatorIntegrationEventContext context(final Optional<OperatorDetails> operatorDetails) {
        final StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();
        beanFactory.addBean("operatorContextProvider", (OperatorContextProvider) () -> operatorDetails);
        return new OperatorIntegrationEventContext(beanFactory.getBeanProvider(OperatorContextProvider.class));
    }
}
