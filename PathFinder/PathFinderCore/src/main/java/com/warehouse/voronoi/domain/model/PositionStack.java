package com.warehouse.voronoi.domain.model;

public class PositionStack {
    private Long id;
    private String token;
    private boolean valid;

    public PositionStack() {
    }

    public PositionStack(final Long id, final String token, final boolean valid) {
        this.id = id;
        this.token = token;
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }
}
