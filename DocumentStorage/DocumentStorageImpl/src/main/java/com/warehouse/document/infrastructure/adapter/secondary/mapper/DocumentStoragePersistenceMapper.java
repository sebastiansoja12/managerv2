package com.warehouse.document.infrastructure.adapter.secondary.mapper;

import com.warehouse.document.domain.model.StoredDocument;
import com.warehouse.document.domain.model.StoredDocumentFile;
import com.warehouse.document.infrastructure.adapter.secondary.entity.StoredDocumentEntity;

public class DocumentStoragePersistenceMapper {

    public StoredDocumentEntity toEntity(final StoredDocumentFile documentFile) {
        final StoredDocument document = documentFile.document();
        return new StoredDocumentEntity(document.documentId(), document.filename(), document.contentType(),
                document.sizeBytes(), documentFile.content(),
                document.uploadedByUserId(), document.createdAt());
    }

    public StoredDocument toDomain(final StoredDocumentEntity entity) {
        return new StoredDocument(entity.getDocumentId(), entity.getFilename(), entity.getContentType(),
                entity.getSizeBytes(), entity.getUploadedByUserId(), entity.getCreatedAt());
    }

    public StoredDocumentFile toFile(final StoredDocumentEntity entity) {
        return new StoredDocumentFile(toDomain(entity), entity.getContent());
    }
}
