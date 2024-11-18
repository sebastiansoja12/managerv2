package com.warehouse.commonassets.model;

public class SoftwareProperty {
    private String category;
    private String name;
    private String value;

    public SoftwareProperty(final String category, final String name, final String value) {
        this.category = category;
        this.name = name;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
