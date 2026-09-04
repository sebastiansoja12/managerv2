package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PalletTransportRepository {
    PalletCarrier append(final PalletId palletId, final PalletTrackingNumber trackingNumber, final PalletCarrier carrier);
    Optional<PalletCarrier> findCurrent(final PalletId palletId);
    Map<PalletId, PalletCarrier> findCurrent(final Collection<PalletId> palletIds);
    List<PalletCarrier> findHistory(final PalletId palletId);
}
