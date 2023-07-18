package com.warehouse.positionstack;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "positionstack")
public class PositionStackProperties {

    private static final String URL = "positionstack.url";

    private static final String STAGE = "positionstack.stage";

    private static final String TOKEN = "positionstack.token";

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

    public String createRequest(String value) {
        return getUrl() + DIVIDER + ACCESS_KEY + token + QUERY + value;
    }
}
