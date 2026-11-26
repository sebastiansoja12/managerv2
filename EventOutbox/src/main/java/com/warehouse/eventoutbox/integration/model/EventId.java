package com.warehouse.eventoutbox.integration.model;

import jakarta.persistence.Embeddable;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record EventId(@JdbcTypeCode(SqlTypes.VARCHAR) UUID value) implements Serializable {

    public static EventId nextId() {
        return new EventId(UUID.randomUUID());
    }
}
