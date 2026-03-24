package com.warehouse.commonassets.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProblemDetails {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private Map<String, Object> properties;

    public ProblemDetails() {
        this.type = "about:blank";
        this.properties = new LinkedHashMap<>();
    }

    public ProblemDetails(final String type,
                          final String title,
                          final Integer status,
                          final String detail,
                          final String instance,
                          final Map<String, Object> properties) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.properties = properties != null ? new LinkedHashMap<>(properties) : new LinkedHashMap<>();
    }

    public static ProblemDetails fromException(final ProblemDetailsException exception, final String fallbackInstance) {
        final String resolvedType = exception.getType() != null ? exception.getType() : "about:blank";
        final String resolvedInstance = exception.getInstance() != null ? exception.getInstance() : fallbackInstance;
        return new ProblemDetails(
                resolvedType,
                exception.getTitle(),
                exception.getStatus(),
                exception.getDetail(),
                resolvedInstance,
                exception.getProperties());
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(final String instance) {
        this.instance = instance;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, Object> properties) {
        this.properties = properties;
    }
}
