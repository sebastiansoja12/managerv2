package com.warehouse.shipment.domain.vo;

public record LocationInfo(Address senderAddress, Address recipientAddress) {
    public static LocationInfo from(final Sender sender, final Recipient recipient) {
        return new LocationInfo(Address.from(sender), Address.from(recipient));
    }
}
