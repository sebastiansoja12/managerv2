package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;

@Repository
public interface PalletDocumentReadRepository extends MongoRepository<PalletDocument, PalletId> {
}
