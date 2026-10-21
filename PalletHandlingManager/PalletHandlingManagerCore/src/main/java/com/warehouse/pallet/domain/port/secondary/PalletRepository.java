package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;

import java.util.Optional;

public interface PalletRepository {
    void createOrUpdate(final Pallet pallet);
    Pallet findById(final PalletId palletId);
    Optional<Pallet> findByTrackingNumber(final TrackingNumber trackingNumber);
    boolean existsByTrackingNumber(final TrackingNumber trackingNumber);
    boolean existsBySealNumber(final SealNumber sealNumber);
    Optional<PalletId> findActivePalletIdContaining(final ShipmentId shipmentId);
    PalletPage search(final PalletSearchCriteria criteria);
}
