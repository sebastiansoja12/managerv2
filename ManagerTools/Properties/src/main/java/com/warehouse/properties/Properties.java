package com.warehouse.properties;

import static javax.naming.InitialContext.doLookup;

import lombok.SneakyThrows;

public class Properties {

    private static final String JNDI_TEMPLATE = "jndi";

    @SneakyThrows
    protected String getStringPropertyValue(String jndi, String value) {
        return JNDI_TEMPLATE.equals(value) ? ((String) doLookup(jndi)).trim() : value;
    }

    @SneakyThrows
    protected String getIntegerPropertyValue(String jndi, String value) {
        return JNDI_TEMPLATE.equals(value) ? String.valueOf((Integer) doLookup(jndi)) : value;
    }

    @SneakyThrows
    protected String getBooleanPropertyValue(String jndi, String value) {
        return JNDI_TEMPLATE.equals(value) ? String.valueOf((Boolean) doLookup(jndi)) : value;
    }

}
