package com.warehouse.document.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentStorageApiService {

    void save(final DocumentFile documentFile);

    List<DocumentMetadata> findMetadata(final List<UUID> documentIds);

    Optional<DocumentFile> findFile(final UUID documentId);
}
