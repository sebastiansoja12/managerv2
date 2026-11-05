package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class CommunicationConfiguration {

    private final String senderName;
    private final String replyToEmail;
    private final String emailFooter;
    private final String logoUrl;
    private final String accentColor;
    private final CustomerLanguage defaultCustomerLanguage;
    private final int chatMessageRetentionDays;
    private final boolean chatAttachmentsAllowed;
    private final int chatMaxAttachmentSizeMb;

    public CommunicationConfiguration(final String senderName,
                                      final String replyToEmail,
                                      final String emailFooter,
                                      final String logoUrl,
                                      final String accentColor,
                                      final CustomerLanguage defaultCustomerLanguage,
                                      final int chatMessageRetentionDays,
                                      final boolean chatAttachmentsAllowed,
                                      final int chatMaxAttachmentSizeMb) {
        this.senderName = senderName;
        this.replyToEmail = replyToEmail;
        this.emailFooter = emailFooter;
        this.logoUrl = logoUrl;
        this.accentColor = accentColor;
        this.defaultCustomerLanguage = defaultCustomerLanguage;
        this.chatMessageRetentionDays = chatMessageRetentionDays;
        this.chatAttachmentsAllowed = chatAttachmentsAllowed;
        this.chatMaxAttachmentSizeMb = chatMaxAttachmentSizeMb;
    }

    public static CommunicationConfiguration defaultConfiguration() {
        return new CommunicationConfiguration(
                "",
                "",
                "",
                "",
                "#1E88E5",
                CustomerLanguage.PL,
                365,
                true,
                10
        );
    }

    public String getSenderName() { return senderName; }
    public String getReplyToEmail() { return replyToEmail; }
    public String getEmailFooter() { return emailFooter; }
    public String getLogoUrl() { return logoUrl; }
    public String getAccentColor() { return accentColor; }
    public CustomerLanguage getDefaultCustomerLanguage() { return defaultCustomerLanguage; }
    public int getChatMessageRetentionDays() { return chatMessageRetentionDays; }
    public boolean isChatAttachmentsAllowed() { return chatAttachmentsAllowed; }
    public int getChatMaxAttachmentSizeMb() { return chatMaxAttachmentSizeMb; }
}
