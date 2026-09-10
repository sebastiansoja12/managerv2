package com.warehouse.commonassets.event.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IntegrationEventKey {

    @JsonIgnore
    String eventKey();
}
