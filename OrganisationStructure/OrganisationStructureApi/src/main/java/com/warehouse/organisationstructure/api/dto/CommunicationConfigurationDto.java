package com.warehouse.organisationstructure.api.dto;

public record CommunicationConfigurationDto(
        String senderName,
        String replyToEmail,
        String emailFooter,
        String logoUrl,
        String accentColor,
        CustomerLanguageDto defaultCustomerLanguage,
        int chatMessageRetentionDays,
        boolean chatAttachmentsAllowed,
        int chatMaxAttachmentSizeMb
) {
}
