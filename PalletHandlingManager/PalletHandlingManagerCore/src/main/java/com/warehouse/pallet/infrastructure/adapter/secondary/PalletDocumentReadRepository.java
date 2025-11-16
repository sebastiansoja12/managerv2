package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.vo.SealNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;

import java.util.Optional;

@Repository
public interface PalletDocumentReadRepository extends MongoRepository<PalletDocument, PalletId> {
    Optional<PalletDocument> findByPalletId(final PalletId palletId);
    boolean existsBySealNumber(final SealNumber sealNumber);
}
