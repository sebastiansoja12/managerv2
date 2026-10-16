package com.warehouse.document.api;

public record DocumentFile(DocumentMetadata metadata, byte[] content) {
}
