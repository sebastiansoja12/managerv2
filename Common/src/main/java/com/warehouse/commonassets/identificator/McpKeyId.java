package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record McpKeyId(UUID value) implements Serializable {

    public static McpKeyId generate() {
        return new McpKeyId(UUID.randomUUID());
    }
}
