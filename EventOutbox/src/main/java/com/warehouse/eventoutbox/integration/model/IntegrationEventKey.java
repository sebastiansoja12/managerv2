package com.warehouse.eventoutbox.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IntegrationEventKey {

    @JsonIgnore
    String eventKey();
}
