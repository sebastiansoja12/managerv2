package com.warehouse.eventoutbox.consumption;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.kafka.common.header.Headers;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.config.MultiMethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntegrationEventListenerRegistrar implements BeanPostProcessor, KafkaListenerConfigurer, BeanFactoryAware {

    private final ObjectProvider<IntegrationEventRegistry> registry;
    private final List<ListenerMethod> listenerMethods = new ArrayList<>();

    private BeanFactory beanFactory;

    public IntegrationEventListenerRegistrar(final ObjectProvider<IntegrationEventRegistry> registry) {
        this.registry = registry;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        MethodIntrospector.selectMethods(targetClass,
                        (MethodIntrospector.MetadataLookup<IntegrationEventListener>) method ->
                                AnnotatedElementUtils.findMergedAnnotation(method, IntegrationEventListener.class))
                .forEach((method, listener) -> this.listenerMethods.add(new ListenerMethod(beanName, bean, method,
                        AopUtils.selectInvocableMethod(method, bean.getClass()), listener)));
        return bean;
    }

    @Override
    public void configureKafkaListeners(final KafkaListenerEndpointRegistrar registrar) {
        if (this.listenerMethods.isEmpty()) {
            return;
        }
        final IntegrationEventRegistry eventRegistry = this.registry.getObject();
        final IntegrationEventTypeResolver typeResolver = new IntegrationEventTypeResolver(() -> eventRegistry);
        final DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = messageHandlerMethodFactory();
        final Map<String, String> endpointsByGroupAndTopic = new HashMap<>();

        listenerGroups(eventRegistry).forEach(listenerGroup -> {
            listenerGroup.requireUnambiguousEventTypes();
            listenerGroup.topics().forEach(topic -> {
                final String previous = endpointsByGroupAndTopic.putIfAbsent(
                        listenerGroup.group() + "|" + topic, listenerGroup.name());
                if (previous != null) {
                    throw new IllegalStateException("Listeners " + previous + " and " + listenerGroup.name()
                            + " use the same group " + listenerGroup.group() + " on topic " + topic
                            + " and would split its partitions; use separate groups or one listener class");
                }
            });

            final MethodKafkaListenerEndpoint<String, String> endpoint = listenerGroup.endpoint();
            endpoint.setId("integration-event-listener." + listenerGroup.beanName() + "." + listenerGroup.group());
            endpoint.setBean(listenerGroup.bean());
            endpoint.setBeanFactory(this.beanFactory);
            endpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
            endpoint.setGroupId(listenerGroup.group());
            endpoint.setTopics(listenerGroup.topics().toArray(String[]::new));
            endpoint.setRecordFilterStrategy(record -> !listenerGroup.accepts(typeResolver, record.headers()));
            endpoint.setAckDiscarded(true);
            registrar.registerEndpoint(endpoint);
            listenerGroup.members().forEach(member -> log.info("[listeners] {} group={} topics={} types={}",
                    member.method().name(), listenerGroup.group(), member.subscription().topics(),
                    member.subscription().types()));
        });
    }

    private List<ListenerGroup> listenerGroups(final IntegrationEventRegistry eventRegistry) {
        final Map<String, List<ListenerGroupMember>> membersByBeanAndGroup = new LinkedHashMap<>();
        this.listenerMethods.forEach(listenerMethod -> membersByBeanAndGroup
                .computeIfAbsent(listenerMethod.beanName() + "|" + listenerMethod.listener().group(),
                        key -> new ArrayList<>())
                .add(new ListenerGroupMember(listenerMethod, IntegrationEventSubscription.from(
                        eventRegistry, listenerMethod.method(), listenerMethod.listener()))));
        return membersByBeanAndGroup.values().stream().map(ListenerGroup::new).toList();
    }

    private DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        if (this.beanFactory instanceof final ConfigurableBeanFactory configurableBeanFactory) {
            factory.setBeanFactory(configurableBeanFactory);
        }
        factory.setCustomArgumentResolvers(List.of(new EventMetadataArgumentResolver()));
        factory.afterPropertiesSet();
        return factory;
    }

    private record ListenerMethod(String beanName,
                                  Object bean,
                                  Method method,
                                  Method invocableMethod,
                                  IntegrationEventListener listener) {

        private String name() {
            return this.method.getDeclaringClass().getSimpleName() + "#" + this.method.getName()
                    + "(" + this.method.getParameterTypes()[0].getSimpleName() + ")";
        }
    }

    private record ListenerGroupMember(ListenerMethod method, IntegrationEventSubscription subscription) {
    }

    private record ListenerGroup(List<ListenerGroupMember> members) {

        private String beanName() {
            return this.members.getFirst().method().beanName();
        }

        private Object bean() {
            return this.members.getFirst().method().bean();
        }

        private String group() {
            return this.members.getFirst().subscription().group();
        }

        private String name() {
            return this.members.getFirst().method().method().getDeclaringClass().getSimpleName()
                    + "[group=" + group() + "]";
        }

        private Set<String> topics() {
            final Set<String> topics = new TreeSet<>();
            this.members.forEach(member -> topics.addAll(member.subscription().topics()));
            return topics;
        }

        private boolean accepts(final IntegrationEventTypeResolver typeResolver,
                                final Headers headers) {
            return this.members.stream()
                    .anyMatch(member -> member.subscription().accepts(typeResolver.resolve(headers)));
        }

        private void requireUnambiguousEventTypes() {
            for (final ListenerGroupMember first : this.members) {
                for (final ListenerGroupMember second : this.members) {
                    final Class<?> firstType = first.subscription().eventType();
                    final Class<?> secondType = second.subscription().eventType();
                    if (first != second && firstType.isAssignableFrom(secondType)) {
                        throw new IllegalStateException("Listener methods " + first.method().name() + " and "
                                + second.method().name() + " in group " + group()
                                + " accept overlapping event types; listen to concrete event types instead");
                    }
                }
            }
        }

        private MethodKafkaListenerEndpoint<String, String> endpoint() {
            if (this.members.size() == 1) {
                final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
                endpoint.setMethod(this.members.getFirst().method().invocableMethod());
                return endpoint;
            }
            return new MultiMethodKafkaListenerEndpoint<>(
                    this.members.stream().map(member -> member.method().invocableMethod()).toList(),
                    null,
                    bean());
        }
    }
}
