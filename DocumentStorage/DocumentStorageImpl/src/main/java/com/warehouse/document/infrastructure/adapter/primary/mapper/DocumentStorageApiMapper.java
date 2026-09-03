package com.warehouse.document.infrastructure.adapter.primary.mapper;

import com.warehouse.document.api.DocumentFile;
import com.warehouse.document.api.DocumentMetadata;
import com.warehouse.document.domain.model.StoredDocument;
import com.warehouse.document.domain.model.StoredDocumentFile;

public class DocumentStorageApiMapper {

    public StoredDocumentFile toDomain(final DocumentFile documentFile) {
        final DocumentMetadata metadata = documentFile.metadata();
        return new StoredDocumentFile(new StoredDocument(metadata.documentId(), metadata.filename(),
                metadata.contentType(), metadata.sizeBytes(), metadata.uploadedByUserId(), metadata.createdAt()),
                documentFile.content());
    }

    public DocumentMetadata toMetadata(final StoredDocument document) {
        return new DocumentMetadata(document.documentId(), document.filename(), document.contentType(),
                document.sizeBytes(), document.uploadedByUserId(), document.createdAt());
    }

    public DocumentFile toFile(final StoredDocumentFile documentFile) {
        return new DocumentFile(toMetadata(documentFile.document()), documentFile.content());
    }
}
