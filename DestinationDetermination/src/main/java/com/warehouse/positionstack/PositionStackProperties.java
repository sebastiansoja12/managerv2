package com.warehouse.positionstack;

import com.warehouse.properties.Properties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "positionstack")
public class PositionStackProperties extends Properties {

    private final static String DIVIDER = "?";

    private final static String ACCESS_KEY = "access_key=";

    private final static String QUERY = "&query=";

    private String url;

    private String stage;

    private String token;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String createRequest(final String token, final String value) {
        return getUrl() + DIVIDER + ACCESS_KEY + token + QUERY + value;
    }
}
