package com.warehouse.eventoutbox.consumption;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

public record IntegrationEventSubscription(String group,
                                           Class<?> eventType,
                                           Set<String> topics,
                                           Set<String> types,
                                           boolean explicitTypes) {

    public static IntegrationEventSubscription from(final IntegrationEventRegistry registry,
                                                    final Method method,
                                                    final IntegrationEventListener listener) {
        if (method.getParameterCount() == 0) {
            throw new IllegalStateException("@IntegrationEventListener method must declare the event parameter: "
                    + method);
        }
        final Class<?> eventType = method.getParameterTypes()[0];
        final Set<IntegrationEventDefinition<?>> definitions = registry.definitions().stream()
                .filter(definition -> eventType.isAssignableFrom(definition.eventClass()))
                .collect(Collectors.toSet());
        final Set<String> topics = listener.topics().length > 0
                ? new TreeSet<>(Set.of(listener.topics()))
                : definitions.stream().map(IntegrationEventDefinition::topic).collect(Collectors.toCollection(TreeSet::new));
        final boolean explicitTypes = listener.types().length > 0;
        final Set<String> types = explicitTypes
                ? new TreeSet<>(Set.of(listener.types()))
                : definitions.stream()
                        .filter(definition -> topics.contains(definition.topic()))
                        .map(IntegrationEventDefinition::type)
                        .collect(Collectors.toCollection(TreeSet::new));
        if (topics.isEmpty()) {
            throw new IllegalStateException("No @IntegrationEventType event is assignable to "
                    + eventType.getName() + "; declare topics and types on " + method);
        }
        return new IntegrationEventSubscription(listener.group(), eventType, topics, types, explicitTypes);
    }

    public boolean accepts(final Optional<IntegrationEventDefinition<?>> definition) {
        return definition
                .map(eventDefinition -> this.explicitTypes
                        ? this.types.contains(eventDefinition.type())
                        : this.eventType.isAssignableFrom(eventDefinition.eventClass()))
                .orElse(true);
    }
}
