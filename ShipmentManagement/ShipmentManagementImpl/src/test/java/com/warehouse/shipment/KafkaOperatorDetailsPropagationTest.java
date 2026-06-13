package com.warehouse.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.event.integration.context.OperatorAwareEvent;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaOperatorContextRecordInterceptor;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KafkaOperatorDetailsPropagationTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPublishShipmentReadModelEventWithCompleteOperatorContextInHeaders() throws Exception {
        final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        final ObjectMapper objectMapper = mock(ObjectMapper.class);
        final ObjectProvider<OperatorContextProvider> contextProviderHolder = mock(ObjectProvider.class);
        final OperatorContextProvider contextProvider = mock(OperatorContextProvider.class);
        final Environment environment = mock(Environment.class);
        final CompletableFuture<SendResult<String, String>> pendingSend = new CompletableFuture<>();
        final OperatorId operatorId = OperatorId.of(11L);
        final UserId userId = new UserId(22L);
        final DepartmentId departmentId = new DepartmentId(33L);

        when(contextProviderHolder.getIfAvailable()).thenReturn(contextProvider);
        when(contextProvider.currentContext()).thenReturn(Optional.of(
                new OperatorDetails(operatorId, userId, departmentId)));
        when(environment.resolvePlaceholders(any())).thenReturn("shipment.read-model.sync");
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(pendingSend);

        final KafkaTemplateClient client = new KafkaTemplateClient(
                kafkaTemplate, objectMapper, contextProviderHolder, environment);
        final ShipmentReadModelChanged event = new ShipmentReadModelChanged(null, Instant.EPOCH);

        client.publish("shipment.read-model.sync", "shipment-1", event);

        final ArgumentCaptor<ProducerRecord<String, String>> recordCaptor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTemplate).send(recordCaptor.capture());
        final ProducerRecord<String, String> record = recordCaptor.getValue();

        assertThat(event).isInstanceOf(OperatorAwareEvent.class);
        assertThat(header(record, KafkaEventHeaders.OPERATOR_ID)).isEqualTo("11");
        assertThat(header(record, KafkaEventHeaders.USER_ID)).isEqualTo("22");
        assertThat(header(record, KafkaEventHeaders.DEPARTMENT_ID)).isEqualTo("33");
    }

    @Test
    void shouldRestoreCompleteOperatorContextBeforeHandlingKafkaRecord() {
        final OperatorContext operatorContext = new OperatorContext();
        final KafkaOperatorContextRecordInterceptor interceptor =
                new KafkaOperatorContextRecordInterceptor(operatorContext);
        final ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "shipment.read-model.sync", 0, 0L, "shipment-1", "{}");
        addHeader(record, KafkaEventHeaders.OPERATOR_ID, 11L);
        addHeader(record, KafkaEventHeaders.USER_ID, 22L);
        addHeader(record, KafkaEventHeaders.DEPARTMENT_ID, 33L);

        interceptor.intercept(record, null);

        final UsernameTenantPasswordAuthenticationToken authentication =
                (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getOperatorId()).isEqualTo(OperatorId.of(11L));
        assertThat(authentication.getPrincipal()).isEqualTo(new UserId(22L));
        assertThat(authentication.getDepartmentId()).isEqualTo(new DepartmentId(33L));

        interceptor.afterRecord(record, null);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private void addHeader(final ConsumerRecord<String, String> record, final String name, final Long value) {
        record.headers().add(name, String.valueOf(value).getBytes(StandardCharsets.UTF_8));
    }

    private String header(final ProducerRecord<String, String> record, final String name) {
        return new String(record.headers().lastHeader(name).value(), StandardCharsets.UTF_8);
    }
}
