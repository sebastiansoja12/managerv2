package com.warehouse.tracking.domain.model;

public record TrackingLocation(String id,
                               String type,
                               String name,
                               String address,
                               String postalCode,
                               String city,
                               String country,
                               String description) {
}
