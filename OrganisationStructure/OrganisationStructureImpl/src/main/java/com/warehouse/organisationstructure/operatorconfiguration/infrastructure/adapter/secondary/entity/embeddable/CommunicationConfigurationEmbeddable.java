package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CustomerLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class CommunicationConfigurationEmbeddable {

    @Column(name = "communication_sender_name")
    private String senderName;

    @Column(name = "communication_reply_to_email")
    private String replyToEmail;

    @Column(name = "communication_email_footer")
    private String emailFooter;

    @Column(name = "communication_logo_url")
    private String logoUrl;

    @Column(name = "communication_accent_color")
    private String accentColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_default_customer_language")
    private CustomerLanguage defaultCustomerLanguage;

    @Column(name = "communication_chat_message_retention_days")
    private int chatMessageRetentionDays;

    @Column(name = "communication_chat_attachments_allowed")
    private boolean chatAttachmentsAllowed;

    @Column(name = "communication_chat_max_attachment_size_mb")
    private int chatMaxAttachmentSizeMb;

    public CommunicationConfigurationEmbeddable() {
    }

    public static CommunicationConfigurationEmbeddable from(final CommunicationConfiguration configuration) {
        final CommunicationConfiguration source = configuration != null
                ? configuration
                : CommunicationConfiguration.defaultConfiguration();
        final CommunicationConfigurationEmbeddable embeddable = new CommunicationConfigurationEmbeddable();
        embeddable.senderName = source.getSenderName();
        embeddable.replyToEmail = source.getReplyToEmail();
        embeddable.emailFooter = source.getEmailFooter();
        embeddable.logoUrl = source.getLogoUrl();
        embeddable.accentColor = source.getAccentColor();
        embeddable.defaultCustomerLanguage = source.getDefaultCustomerLanguage();
        embeddable.chatMessageRetentionDays = source.getChatMessageRetentionDays();
        embeddable.chatAttachmentsAllowed = source.isChatAttachmentsAllowed();
        embeddable.chatMaxAttachmentSizeMb = source.getChatMaxAttachmentSizeMb();
        return embeddable;
    }

    public CommunicationConfiguration toModel() {
        return new CommunicationConfiguration(
                senderName,
                replyToEmail,
                emailFooter,
                logoUrl,
                accentColor,
                defaultCustomerLanguage,
                chatMessageRetentionDays,
                chatAttachmentsAllowed,
                chatMaxAttachmentSizeMb
        );
    }
}
