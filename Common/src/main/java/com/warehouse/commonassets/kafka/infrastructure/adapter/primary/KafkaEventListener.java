package com.warehouse.commonassets.kafka.infrastructure.adapter.primary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@KafkaListener
public @interface KafkaEventListener {

    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] value() default {};

    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics() default {};

    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "${spring.application.name}";
}
