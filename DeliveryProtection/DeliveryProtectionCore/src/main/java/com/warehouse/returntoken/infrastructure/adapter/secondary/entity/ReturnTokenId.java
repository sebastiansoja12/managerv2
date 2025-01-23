package com.warehouse.returntoken.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record ReturnTokenId(Long value) {
}
