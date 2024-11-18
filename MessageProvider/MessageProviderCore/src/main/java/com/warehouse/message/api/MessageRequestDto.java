package com.warehouse.message.api;

public final class MessageRequestDto {
    
    private TitleDto title;

    private ShipmentStatusDto shipmentStatus;

    private LanguageDto language;

    private MessageContentDto messageContent;

    private SenderDto sender;

    public MessageRequestDto() {
    }

    public MessageRequestDto(final TitleDto title,
                             final ShipmentStatusDto shipmentStatus,
                             final LanguageDto language,
                             final MessageContentDto messageContent,
                             final SenderDto sender) {
        this.title = title;
        this.shipmentStatus = shipmentStatus;
        this.language = language;
        this.messageContent = messageContent;
        this.sender = sender;
    }

    public TitleDto getTitle() {
        return title;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public LanguageDto getLanguage() {
        return language;
    }

    public MessageContentDto getMessageContent() {
        return messageContent;
    }

    public SenderDto getSender() {
        return sender;
    }
}
