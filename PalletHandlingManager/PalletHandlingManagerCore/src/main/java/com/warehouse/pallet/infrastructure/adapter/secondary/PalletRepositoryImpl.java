package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;

public class PalletRepositoryImpl implements PalletRepository {

    private final PalletDocumentReadRepository repository;

    public PalletRepositoryImpl(final PalletDocumentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createOrUpdate(final Pallet pallet) {
        final PalletDocument palletDocument = PalletDocument.from(pallet);
        this.repository.save(palletDocument);
    }

    @Override
    public Pallet findById(final PalletId palletId) {
        return this.repository
                .findById(palletId)
                .map(Pallet::from)
                .orElse(null);
    }
}
