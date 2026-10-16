package com.warehouse.document.infrastructure.adapter.primary;

import com.warehouse.document.api.DocumentFile;
import com.warehouse.document.api.DocumentMetadata;
import com.warehouse.document.api.DocumentStorageApiService;
import com.warehouse.document.application.port.primary.DocumentStoragePort;
import com.warehouse.document.infrastructure.adapter.primary.mapper.DocumentStorageApiMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentStorageServiceAdapter implements DocumentStorageApiService {

    private final DocumentStoragePort documentStoragePort;
    private final DocumentStorageApiMapper mapper;

    public DocumentStorageServiceAdapter(final DocumentStoragePort documentStoragePort,
                                         final DocumentStorageApiMapper mapper) {
        this.documentStoragePort = documentStoragePort;
        this.mapper = mapper;
    }

    @Override
    public void save(final DocumentFile documentFile) {
        documentStoragePort.save(mapper.toDomain(documentFile));
    }

    @Override
    public List<DocumentMetadata> findMetadata(final List<UUID> documentIds) {
        return documentStoragePort.findDocuments(documentIds).stream().map(mapper::toMetadata).toList();
    }

    @Override
    public Optional<DocumentFile> findFile(final UUID documentId) {
        return documentStoragePort.findFile(documentId).map(mapper::toFile);
    }
}
