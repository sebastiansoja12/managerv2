package com.warehouse.document.application.port.secondary;

import com.warehouse.document.domain.model.StoredDocument;
import com.warehouse.document.domain.model.StoredDocumentFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentStorageRepository {

    void save(final StoredDocumentFile documentFile);

    List<StoredDocument> findDocuments(final List<UUID> documentIds);

    Optional<StoredDocumentFile> findFile(final UUID documentId);
}
