package com.warehouse.routetracker.infrastructure.adapter.primary.api;

public record Person(String firstName, String lastName, String email, String telephoneNumber, String city,
                     String postalCode, String street) {
}
