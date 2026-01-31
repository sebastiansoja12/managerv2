package com.warehouse.commonassets.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProblemDetailsException extends RuntimeException {

    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final Map<String, Object> properties;

    public ProblemDetailsException(final int status, final String detail) {
        this("about:blank", "Request failed", status, detail, null, null);
    }

    public ProblemDetailsException(final String type,
                                   final String title,
                                   final int status,
                                   final String detail) {
        this(type, title, status, detail, null, null);
    }

    public ProblemDetailsException(final String type,
                                   final String title,
                                   final int status,
                                   final String detail,
                                   final String instance,
                                   final Map<String, Object> properties) {
        super(detail);
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.properties = properties != null ? new LinkedHashMap<>(properties) : new LinkedHashMap<>();
    }

    public ProblemDetailsException withProperty(final String key, final Object value) {
        final Map<String, Object> merged = new LinkedHashMap<>(this.properties);
        merged.put(key, value);
        return new ProblemDetailsException(this.type, this.title, this.status, this.detail, this.instance, merged);
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
