package com.warehouse.document.domain.model;

import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record StoredDocument(UUID documentId, String filename, String contentType,
                             long sizeBytes, UserId uploadedByUserId, Instant createdAt) {
}
