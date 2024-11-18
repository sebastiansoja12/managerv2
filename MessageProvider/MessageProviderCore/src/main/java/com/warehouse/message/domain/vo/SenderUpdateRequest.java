package com.warehouse.message.domain.vo;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.SenderUpdateRequestDto;

public record SenderUpdateRequest(MessageId messageId, String sender) {
    public static SenderUpdateRequest from(final SenderUpdateRequestDto senderUpdateRequest) {
        final MessageId id = new MessageId(senderUpdateRequest.messageId().value());
        final String sender = senderUpdateRequest.sender().value();
        return new SenderUpdateRequest(id, sender);
    }
}
