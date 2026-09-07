package com.warehouse.chat.api.identificator;

import java.util.UUID;

final class ChatIdentifierValue {

    private ChatIdentifierValue() {
    }

    static Long generate() {
        long value;
        do {
            value = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        } while (value == 0L);
        return value;
    }
}
