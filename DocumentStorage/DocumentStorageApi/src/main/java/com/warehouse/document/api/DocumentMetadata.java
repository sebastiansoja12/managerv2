package com.warehouse.document.api;

import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record DocumentMetadata(UUID documentId, String filename, String contentType,
                               long sizeBytes, UserId uploadedByUserId, Instant createdAt) {
}
