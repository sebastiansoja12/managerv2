package com.warehouse.redirect.infrastructure.adapter.secondary.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "redirect.token")
public class RedirectTokenProperties {

    private String body;

    private String subject;

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
