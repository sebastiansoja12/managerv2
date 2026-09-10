package com.warehouse.chat.domain.vo;

import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.commonassets.identificator.OperatorId;

import static com.warehouse.chat.domain.exception.ChatException.Reason.ACCESS_DENIED;
import static com.warehouse.chat.domain.exception.ChatException.Reason.NOT_FOUND;

public record ChatRecipient(OperatorId operatorId, boolean active) {

    public void requireAvailableTo(final OperatorId requestingOperatorId) {
        if (!active) {
            throw new ChatException(NOT_FOUND, "Chat recipient was not found or is inactive");
        }
        if (!requestingOperatorId.equals(operatorId)) {
            throw new ChatException(ACCESS_DENIED, "Chat recipient belongs to another organization");
        }
    }
}
