package com.warehouse.shipment.application.event.snapshot;

import com.warehouse.shipment.domain.vo.Sender;

public record SenderSnapshot(
        String firstName,
        String lastName,
        String email,
        String telephoneNumber,
        String city,
        String postalCode,
        String street
) {

    public static SenderSnapshot from(final Sender sender) {
        if (sender == null) {
            return null;
        }
        return new SenderSnapshot(
                sender.getFirstName(),
                sender.getLastName(),
                sender.getEmail(),
                sender.getTelephoneNumber(),
                sender.getCity(),
                sender.getPostalCode(),
                sender.getStreet()
        );
    }
}
