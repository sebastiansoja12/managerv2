package com.warehouse.deliveryreject.domain.vo;

public record Person(String firstName, String lastName, String email) {

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
