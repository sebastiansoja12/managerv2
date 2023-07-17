package com.warehouse.positionstack;

import com.warehouse.properties.Properties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "positionstack")
public class PositionStackProperties extends Properties {

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
        return getUrl() + DIVIDER + ACCESS_KEY + getToken() + QUERY + value;
    }

    public String createTemporaryRequest(String value) {
        return "http://api.positionstack.com/v1/forward" + DIVIDER + ACCESS_KEY
                + "5e64e700b4085324cbaa97b59be0e9d0" + QUERY + value;
    }
}
