package com.warehouse.deliveryhandover.domain.vo;

public record RecipientConfirmation(String recipientName, String signature) {

    public boolean signed() {
        return signature != null && !signature.isBlank();
    }

    public boolean hasRecipientName() {
        return recipientName != null && !recipientName.isBlank();
    }
}
