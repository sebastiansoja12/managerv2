package com.warehouse.eventoutbox.consumption;

import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.CREATED_TOPIC;
import static com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.EVENTS_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.MultiMethodKafkaListenerEndpoint;

import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.AmbiguousListeners;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.OtherStatusChangedListener;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.StatusChangedListener;
import com.warehouse.eventoutbox.consumption.IntegrationEventConsumptionFixture.TestListeners;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

class IntegrationEventListenerRegistrarTest {

    @Test
    void shouldRegisterOneEndpointPerListenerBeanAndGroup() {
        final IntegrationEventListenerRegistrar listenerRegistrar = listenerRegistrar();
        listenerRegistrar.postProcessAfterInitialization(new TestListeners(), "testListeners");
        final KafkaListenerEndpointRegistrar endpointRegistrar = mock(KafkaListenerEndpointRegistrar.class);

        listenerRegistrar.configureKafkaListeners(endpointRegistrar);

        final ArgumentCaptor<KafkaListenerEndpoint> endpoints = ArgumentCaptor.forClass(KafkaListenerEndpoint.class);
        verify(endpointRegistrar, times(3)).registerEndpoint(endpoints.capture());
        final Map<String, KafkaListenerEndpoint> endpointsById = endpoints.getAllValues().stream()
                .collect(Collectors.toMap(KafkaListenerEndpoint::getId, Function.identity()));
        final KafkaListenerEndpoint delivery = endpointsById.get("integration-event-listener.testListeners.test-delivery");
        assertThat(delivery).isInstanceOf(MultiMethodKafkaListenerEndpoint.class);
        assertThat(delivery.getGroupId()).isEqualTo("test-delivery");
        assertThat(delivery.getTopics()).containsExactly(CREATED_TOPIC, EVENTS_TOPIC);
        assertThat(((MultiMethodKafkaListenerEndpoint<?, ?>) delivery).getMethods())
                .extracting(Method::getName)
                .containsExactlyInAnyOrder("onCreated", "onDestinationChanged");
        final KafkaListenerEndpoint mail = endpointsById.get("integration-event-listener.testListeners.test-mail");
        assertThat(mail).isNotInstanceOf(MultiMethodKafkaListenerEndpoint.class);
        assertThat(mail.getTopics()).containsExactly(CREATED_TOPIC, EVENTS_TOPIC);
        assertThat(endpointsById.get("integration-event-listener.testListeners.test-tracker").getTopics())
                .containsExactly(EVENTS_TOPIC);
    }

    @Test
    void shouldThrowExceptionWhenTwoListenerBeansShareGroupOnSameTopic() {
        final IntegrationEventListenerRegistrar listenerRegistrar = listenerRegistrar();
        listenerRegistrar.postProcessAfterInitialization(new StatusChangedListener(), "statusChangedListener");
        listenerRegistrar.postProcessAfterInitialization(new OtherStatusChangedListener(), "otherStatusChangedListener");
        final KafkaListenerEndpointRegistrar endpointRegistrar = mock(KafkaListenerEndpointRegistrar.class);

        assertThatThrownBy(() -> listenerRegistrar.configureKafkaListeners(endpointRegistrar))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("test-delivery")
                .hasMessageContaining(EVENTS_TOPIC)
                .hasMessageContaining("StatusChangedListener")
                .hasMessageContaining("OtherStatusChangedListener");
    }

    @Test
    void shouldThrowExceptionWhenListenerMethodsInGroupAcceptOverlappingEventTypes() {
        final IntegrationEventListenerRegistrar listenerRegistrar = listenerRegistrar();
        listenerRegistrar.postProcessAfterInitialization(new AmbiguousListeners(), "ambiguousListeners");
        final KafkaListenerEndpointRegistrar endpointRegistrar = mock(KafkaListenerEndpointRegistrar.class);

        assertThatThrownBy(() -> listenerRegistrar.configureKafkaListeners(endpointRegistrar))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("onAnyChange")
                .hasMessageContaining("onStatusChanged")
                .hasMessageContaining("overlapping");
    }

    @Test
    void shouldRegisterNothingWhenThereAreNoListenerMethods() {
        final IntegrationEventListenerRegistrar listenerRegistrar = listenerRegistrar();
        listenerRegistrar.postProcessAfterInitialization(List.of(), "unrelatedBean");
        final KafkaListenerEndpointRegistrar endpointRegistrar = mock(KafkaListenerEndpointRegistrar.class);

        listenerRegistrar.configureKafkaListeners(endpointRegistrar);

        verify(endpointRegistrar, never()).registerEndpoint(org.mockito.ArgumentMatchers.any());
    }

    private IntegrationEventListenerRegistrar listenerRegistrar() {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("integrationEventRegistry", IntegrationEventConsumptionFixture.registry());
        final ObjectProvider<IntegrationEventRegistry> registry = beanFactory.getBeanProvider(IntegrationEventRegistry.class);
        final IntegrationEventListenerRegistrar listenerRegistrar = new IntegrationEventListenerRegistrar(registry);
        listenerRegistrar.setBeanFactory(beanFactory);
        return listenerRegistrar;
    }
}
