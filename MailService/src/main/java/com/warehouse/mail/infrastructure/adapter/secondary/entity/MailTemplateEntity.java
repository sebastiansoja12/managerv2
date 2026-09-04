package com.warehouse.mail.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "mail.MailTemplateEntity")
@Table(name = "mail_templates")
public class MailTemplateEntity {

    @Id
    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Column(name = "template_key", nullable = false, length = 120)
    private String templateKey;

    @Column(name = "locale", nullable = false, length = 16)
    private String locale;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected MailTemplateEntity() {
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public String getLocale() {
        return locale;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
