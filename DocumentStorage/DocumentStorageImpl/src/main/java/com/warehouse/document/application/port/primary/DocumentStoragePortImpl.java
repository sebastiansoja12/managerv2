package com.warehouse.document.application.port.primary;

import com.warehouse.document.application.port.secondary.DocumentStorageRepository;
import com.warehouse.document.domain.model.StoredDocument;
import com.warehouse.document.domain.model.StoredDocumentFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentStoragePortImpl implements DocumentStoragePort {

    private final DocumentStorageRepository repository;

    public DocumentStoragePortImpl(final DocumentStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(final StoredDocumentFile documentFile) {
        repository.save(documentFile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoredDocument> findDocuments(final List<UUID> documentIds) {
        return repository.findDocuments(documentIds);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoredDocumentFile> findFile(final UUID documentId) {
        return repository.findFile(documentId);
    }
}
