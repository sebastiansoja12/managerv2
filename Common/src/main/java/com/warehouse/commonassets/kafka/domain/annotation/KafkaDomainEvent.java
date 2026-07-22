package com.warehouse.commonassets.kafka.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Repeatable(KafkaDomainEvents.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KafkaDomainEvent {

    String topic();

    String topicProperty() default "";

    int version() default 1;
}
