package com.warehouse.document.infrastructure.adapter.secondary;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.document.application.port.secondary.DocumentStorageRepository;
import com.warehouse.document.domain.model.StoredDocument;
import com.warehouse.document.domain.model.StoredDocumentFile;
import com.warehouse.document.infrastructure.adapter.secondary.entity.StoredDocumentEntity;
import com.warehouse.document.infrastructure.adapter.secondary.mapper.DocumentStoragePersistenceMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentStorageRepositoryImpl implements DocumentStorageRepository {

    private final OperatorFilteredRepository<StoredDocumentEntity> repository;
    private final DocumentStoragePersistenceMapper mapper;

    public DocumentStorageRepositoryImpl(final OperatorFilteredRepository<StoredDocumentEntity> repository,
                                         final DocumentStoragePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(final StoredDocumentFile documentFile) {
        repository.create(mapper.toEntity(documentFile));
    }

    @Override
    public List<StoredDocument> findDocuments(final List<UUID> documentIds) {
        if (documentIds.isEmpty()) {
            return List.of();
        }
        return repository.createCriteria(StoredDocumentEntity.class)
                .in("documentId", documentIds)
                .desc("createdAt")
                .list()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<StoredDocumentFile> findFile(final UUID documentId) {
        return repository.createCriteria(StoredDocumentEntity.class)
                .eq("documentId", documentId)
                .one()
                .map(mapper::toFile);
    }
}
