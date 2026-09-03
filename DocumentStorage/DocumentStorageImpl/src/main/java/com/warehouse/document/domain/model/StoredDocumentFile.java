package com.warehouse.document.domain.model;

public record StoredDocumentFile(StoredDocument document, byte[] content) {

    public StoredDocumentFile {
        content = content.clone();
    }

    @Override
    public byte[] content() {
        return content.clone();
    }
}
