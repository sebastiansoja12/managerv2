package com.warehouse.deliverytoken.infrastructure.adapter.secondary.property;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "sts")
public class STSProperty {

    String url;

    String stage;

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
