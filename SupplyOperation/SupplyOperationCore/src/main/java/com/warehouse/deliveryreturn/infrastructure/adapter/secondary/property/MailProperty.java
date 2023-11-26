package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail")
public class MailProperty {
    private String message;
    private String subject;
}
