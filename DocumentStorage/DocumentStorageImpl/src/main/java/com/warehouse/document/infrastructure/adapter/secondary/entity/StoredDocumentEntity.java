package com.warehouse.document.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "document.StoredDocumentEntity")
@Table(name = "document")
public class StoredDocumentEntity extends BelongsToOperator {

    @Id
    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Column(name = "content", nullable = false, columnDefinition = "bytea")
    private byte[] content;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "uploaded_by_user_id", nullable = false))
    private UserId uploadedByUserId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected StoredDocumentEntity() {
    }

    public StoredDocumentEntity(final UUID documentId, final String filename, final String contentType,
                                final long sizeBytes, final byte[] content,
                                final UserId uploadedByUserId, final Instant createdAt) {
        this.documentId = documentId;
        this.filename = filename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.content = content;
        this.uploadedByUserId = uploadedByUserId;
        this.createdAt = createdAt;
    }

    public UUID getDocumentId() { return documentId; }
    public String getFilename() { return filename; }
    public String getContentType() { return contentType; }
    public long getSizeBytes() { return sizeBytes; }
    public byte[] getContent() { return content; }
    public UserId getUploadedByUserId() { return uploadedByUserId; }
    public Instant getCreatedAt() { return createdAt; }
}
