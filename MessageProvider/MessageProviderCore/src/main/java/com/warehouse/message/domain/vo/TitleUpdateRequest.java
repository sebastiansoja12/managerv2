package com.warehouse.message.domain.vo;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.TitleUpdateRequestDto;

public final class TitleUpdateRequest {
    private final MessageId messageId;
    private final String title;

    public TitleUpdateRequest(final MessageId messageId, final String title) {
        this.messageId = messageId;
        this.title = title;
    }

    public static TitleUpdateRequest from(final TitleUpdateRequestDto titleUpdateRequest) {
        final MessageId id = new MessageId(titleUpdateRequest.messageId().value());
        return new TitleUpdateRequest(id, titleUpdateRequest.title().value());
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public String getTitle() {
        return title;
    }
}
