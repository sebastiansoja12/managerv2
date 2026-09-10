package com.warehouse.driverhub.domain.model;

final class Text {

    private Text() {
    }

    static boolean blank(final String value) {
        return value == null || value.isBlank();
    }

    static String trim(final String value) {
        return blank(value) ? null : value.trim();
    }
}
