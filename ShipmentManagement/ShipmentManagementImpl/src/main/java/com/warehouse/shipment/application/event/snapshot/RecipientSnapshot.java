package com.warehouse.shipment.application.event.snapshot;

import com.warehouse.shipment.domain.vo.Recipient;

public record RecipientSnapshot(
        String firstName,
        String lastName,
        String email,
        String telephoneNumber,
        String city,
        String postalCode,
        String street
) {

    public static RecipientSnapshot from(final Recipient recipient) {
        if (recipient == null) {
            return null;
        }
        return new RecipientSnapshot(
                recipient.getFirstName(),
                recipient.getLastName(),
                recipient.getEmail(),
                recipient.getTelephoneNumber(),
                recipient.getCity(),
                recipient.getPostalCode(),
                recipient.getStreet()
        );
    }
}
