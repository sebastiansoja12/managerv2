package com.warehouse.shipment;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaOperatorContextRecordInterceptor;
import com.warehouse.commonassets.kafka.infrastructure.config.KafkaConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KafkaConfigurationTest {

    private final KafkaConfiguration configuration = new KafkaConfiguration();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void shouldPublishAnyFailedRecordToTopicDltWithOriginalHeadersAndFailureMetadata() {
        final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        final CompletableFuture<SendResult<String, String>> sendResult =
                CompletableFuture.completedFuture(mock(SendResult.class));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn((CompletableFuture) sendResult);
        final DeadLetterPublishingRecoverer recoverer =
                this.configuration.kafkaDeadLetterPublishingRecoverer(kafkaTemplate, ".DLT", 1_000);
        recoverer.setVerifyPartition(false);
        final ConsumerRecord<String, String> failedRecord = new ConsumerRecord<>(
                "any.integration.topic", 2, 15L, "aggregate-1", "{\"payload\":{}}");
        failedRecord.headers().add(
                KafkaEventHeaders.OPERATOR_ID,
                "7".getBytes(StandardCharsets.UTF_8));
        final IllegalStateException failure = new IllegalStateException("database unavailable");

        recoverer.accept(failedRecord, mock(Consumer.class), failure);

        final ArgumentCaptor<ProducerRecord<String, String>> recordCaptor =
                ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTemplate).send(recordCaptor.capture());
        final ProducerRecord<String, String> dltRecord = recordCaptor.getValue();
        assertThat(dltRecord.topic()).isEqualTo("any.integration.topic.DLT");
        assertThat(dltRecord.partition()).isEqualTo(2);
        assertThat(dltRecord.key()).isEqualTo("aggregate-1");
        assertThat(dltRecord.value()).isEqualTo("{\"payload\":{}}");
        assertThat(header(dltRecord, KafkaEventHeaders.OPERATOR_ID)).isEqualTo("7");
        assertThat(header(dltRecord, KafkaHeaders.DLT_ORIGINAL_TOPIC))
                .isEqualTo("any.integration.topic");
        assertThat(header(dltRecord, KafkaHeaders.DLT_EXCEPTION_MESSAGE))
                .contains("database unavailable");
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldRetryEveryConsumerRecordBeforeRecoveringToDlt() {
        final DeadLetterPublishingRecoverer recoverer = mock(DeadLetterPublishingRecoverer.class);
        final DefaultErrorHandler errorHandler = this.configuration.kafkaErrorHandler(recoverer, 3, 0);
        final ConsumerRecord<String, String> failedRecord = new ConsumerRecord<>(
                "any.integration.topic", 0, 1L, "aggregate-1", "{}");
        final Consumer<String, String> consumer = mock(Consumer.class);
        final MessageListenerContainer container = mock(MessageListenerContainer.class);
        when(container.isRunning()).thenReturn(true);
        final IllegalStateException failure = new IllegalStateException("temporary failure");

        assertThat(errorHandler.handleOne(failure, failedRecord, consumer, container)).isFalse();
        assertThat(errorHandler.handleOne(failure, failedRecord, consumer, container)).isFalse();
        assertThat(errorHandler.handleOne(failure, failedRecord, consumer, container)).isTrue();

        verify(recoverer, times(1)).accept(failedRecord, consumer, failure);
        assertThat(errorHandler.isAckAfterHandle()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldConfigureDefaultFactoryWithRetryDltAndRecordAck() {
        final ConsumerFactory<String, String> consumerFactory = mock(ConsumerFactory.class);
        final RecordMessageConverter converter = mock(RecordMessageConverter.class);
        final KafkaOperatorContextRecordInterceptor interceptor =
                mock(KafkaOperatorContextRecordInterceptor.class);
        final DefaultErrorHandler errorHandler = mock(DefaultErrorHandler.class);

        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                this.configuration.kafkaListenerContainerFactory(
                        consumerFactory, converter, interceptor, errorHandler);

        assertThat(factory.getConsumerFactory()).isSameAs(consumerFactory);
        assertThat(factory.getContainerProperties().getAckMode())
                .isEqualTo(ContainerProperties.AckMode.RECORD);
        assertThat(factory.getContainerProperties().isDeliveryAttemptHeader()).isTrue();
        assertThat(factory.createContainer("any.integration.topic").getCommonErrorHandler())
                .isSameAs(errorHandler);
    }

    @Test
    void shouldRejectInvalidGlobalRetryAndDltSettings() {
        final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        final DeadLetterPublishingRecoverer recoverer = mock(DeadLetterPublishingRecoverer.class);

        assertThatThrownBy(() ->
                this.configuration.kafkaDeadLetterPublishingRecoverer(kafkaTemplate, " ", 1_000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("suffix");
        assertThatThrownBy(() ->
                this.configuration.kafkaDeadLetterPublishingRecoverer(kafkaTemplate, ".DLT", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("timeout");
        assertThatThrownBy(() ->
                this.configuration.kafkaErrorHandler(recoverer, 0, 1_000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("max attempts");
        assertThatThrownBy(() ->
                this.configuration.kafkaErrorHandler(recoverer, 3, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("backoff");
    }

    private String header(final ProducerRecord<String, String> record, final String name) {
        final Header header = record.headers().lastHeader(name);
        assertThat(header).isNotNull();
        return new String(header.value(), StandardCharsets.UTF_8);
    }
}
