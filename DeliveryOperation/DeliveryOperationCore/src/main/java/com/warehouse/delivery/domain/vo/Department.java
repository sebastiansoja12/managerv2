package com.warehouse.delivery.domain.vo;

public class Department {
    private final Boolean active;

    public Department(final Boolean active) {
        this.active = active;
    }

    public Boolean isActive() {
        return active;
    }
}
