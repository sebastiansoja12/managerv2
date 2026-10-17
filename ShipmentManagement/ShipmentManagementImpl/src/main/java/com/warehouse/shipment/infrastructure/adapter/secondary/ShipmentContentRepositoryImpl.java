package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.document.api.DocumentStorageApiService;
import com.warehouse.shipment.application.port.secondary.ShipmentContentRepository;
import com.warehouse.shipment.domain.model.ShipmentDocument;
import com.warehouse.shipment.domain.model.ShipmentDocumentFile;
import com.warehouse.shipment.domain.model.ShipmentNote;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentDocumentLinkEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentNoteEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentContentPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ShipmentContentRepositoryImpl implements ShipmentContentRepository {

    private static final String DOCUMENT_IDS_FOR_SHIPMENT = "select link.documentId from shipment.ShipmentDocumentLinkEntity link "
            + "where link.shipmentId.value = :shipmentId and link.operatorId.value = :operatorId";

    private final EntityManager entityManager;
    private final OperatorContextProvider operatorContextProvider;
    private final ShipmentContentPersistenceMapper mapper;
    private final DocumentStorageApiService documentStorageApiService;

    public ShipmentContentRepositoryImpl(final EntityManager entityManager,
                                         final OperatorContextProvider operatorContextProvider,
                                         final ShipmentContentPersistenceMapper mapper,
                                         final DocumentStorageApiService documentStorageApiService) {
        this.entityManager = entityManager;
        this.operatorContextProvider = operatorContextProvider;
        this.mapper = mapper;
        this.documentStorageApiService = documentStorageApiService;
    }

    @Override
    public void saveNote(final ShipmentNote note) {
        entityManager.persist(mapper.toEntity(note, operatorContextProvider.currentOperatorId()));
    }

    @Override
    public List<ShipmentNote> listNotes(final ShipmentId shipmentId) {
        return entityManager.createQuery("select note from shipment.ShipmentNoteEntity note "
                        + "where note.shipmentId.value = :shipmentId and note.operatorId.value = :operatorId "
                        + "order by note.createdAt desc", ShipmentNoteEntity.class)
                .setParameter("shipmentId", shipmentId.getValue())
                .setParameter("operatorId", operatorContextProvider.currentOperatorId().getValue())
                .getResultStream().map(mapper::toDomain).toList();
    }

    @Override
    public void saveDocument(final ShipmentDocumentFile documentFile) {
        final OperatorId operatorId = operatorContextProvider.currentOperatorId();
        final ShipmentDocument document = documentFile.document();
        documentStorageApiService.save(mapper.toDocumentFile(documentFile));
        entityManager.persist(new ShipmentDocumentLinkEntity(UUID.randomUUID(),
                document.shipmentId(), document.documentId(), operatorId));
    }

    @Override
    public List<ShipmentDocument> listDocuments(final ShipmentId shipmentId) {
        final List<UUID> documentIds = entityManager.createQuery(DOCUMENT_IDS_FOR_SHIPMENT, UUID.class)
                .setParameter("shipmentId", shipmentId.getValue())
                .setParameter("operatorId", operatorContextProvider.currentOperatorId().getValue())
                .getResultList();
        return documentStorageApiService.findMetadata(documentIds).stream()
                .map(metadata -> mapper.toDomain(metadata, shipmentId)).toList();
    }

    @Override
    public Optional<ShipmentDocumentFile> findDocument(final ShipmentId shipmentId, final UUID documentId) {
        final boolean linked = entityManager.createQuery(DOCUMENT_IDS_FOR_SHIPMENT
                        + " and link.documentId = :documentId", UUID.class)
                .setParameter("shipmentId", shipmentId.getValue())
                .setParameter("documentId", documentId)
                .setParameter("operatorId", operatorContextProvider.currentOperatorId().getValue())
                .setMaxResults(1)
                .getResultStream().findFirst().isPresent();
        return linked ? documentStorageApiService.findFile(documentId)
                .map(documentFile -> mapper.toDomain(documentFile, shipmentId)) : Optional.empty();
    }
}
