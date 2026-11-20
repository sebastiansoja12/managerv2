package com.warehouse.eventoutbox.publication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

public class IntegrationEventTranslationRegistry implements BeanPostProcessor {

    private final List<Translator> translators = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        final Map<Method, IntegrationEventTranslation> methods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<IntegrationEventTranslation>) method ->
                        AnnotatedElementUtils.findMergedAnnotation(method, IntegrationEventTranslation.class));
        if (methods.isEmpty()) {
            return bean;
        }
        final Map<Class<?>, Method> methodsByDomainEvent = new LinkedHashMap<>();
        methods.keySet().forEach(method -> {
            final Class<?> domainEventType = domainEventType(method);
            final Method invocableMethod = AopUtils.selectInvocableMethod(method, bean.getClass());
            ReflectionUtils.makeAccessible(invocableMethod);
            final Method previous = methodsByDomainEvent.putIfAbsent(domainEventType, invocableMethod);
            if (previous != null) {
                throw new IllegalStateException("Domain event " + domainEventType.getName()
                        + " is translated more than once in " + targetClass.getName());
            }
        });
        this.translators.add(new Translator(bean, targetClass, methodsByDomainEvent));
        return bean;
    }

    public List<IntegrationEvent> translate(final DomainEvent event) {
        final List<IntegrationEvent> integrationEvents = new ArrayList<>();
        this.translators.forEach(translator -> translator.methodFor(event.getClass())
                .ifPresent(method -> integrationEvents.addAll(
                        toIntegrationEvents(ReflectionUtils.invokeMethod(method, translator.bean(), event)))));
        return integrationEvents;
    }

    private Class<?> domainEventType(final Method method) {
        if (method.getParameterCount() != 1 || !DomainEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new IllegalStateException(
                    "@IntegrationEventTranslation method must take exactly one DomainEvent parameter: " + method);
        }
        final Class<?> returnType = method.getReturnType();
        if (!IntegrationEvent.class.isAssignableFrom(returnType) && !Collection.class.isAssignableFrom(returnType)) {
            throw new IllegalStateException(
                    "@IntegrationEventTranslation method must return an IntegrationEvent or a collection of them: "
                            + method);
        }
        return method.getParameterTypes()[0];
    }

    private List<IntegrationEvent> toIntegrationEvents(final Object result) {
        if (result == null) {
            return List.of();
        }
        if (result instanceof final IntegrationEvent integrationEvent) {
            return List.of(integrationEvent);
        }
        return ((Collection<?>) result).stream().map(IntegrationEvent.class::cast).toList();
    }

    private record Translator(Object bean, Class<?> targetClass, Map<Class<?>, Method> methodsByDomainEvent) {

        private Optional<Method> methodFor(final Class<?> eventClass) {
            Class<?> current = eventClass;
            while (current != null && current != Object.class) {
                final Method method = this.methodsByDomainEvent.get(current);
                if (method != null) {
                    return Optional.of(method);
                }
                current = current.getSuperclass();
            }
            return ClassUtils.getAllInterfacesForClassAsSet(eventClass).stream()
                    .map(this.methodsByDomainEvent::get)
                    .filter(Objects::nonNull)
                    .findFirst();
        }
    }
}
