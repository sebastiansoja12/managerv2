package com.warehouse.tools.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail")
public class MailProperty {
    private String message;
    private String subject;
}
