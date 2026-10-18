package com.warehouse.palletmanagement.application.service;

import com.warehouse.palletmanagement.application.port.secondary.PalletTransportRepository;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;

import java.util.List;
import java.util.Map;

public class PalletTransportService {

    private final PalletTransportRepository palletTransportRepository;

    public PalletTransportService(final PalletTransportRepository palletTransportRepository) {
        this.palletTransportRepository = palletTransportRepository;
    }

    public PalletDetails withTransport(final PalletDetails pallet) {
        return pallet.withCarrier(this.palletTransportRepository.findCurrent(pallet.palletId()).orElse(null));
    }

    public PalletPage withTransport(final PalletPage page) {
        final Map<PalletId, PalletCarrier> carriers = this.palletTransportRepository.findCurrent(
                page.pallets().stream().map(PalletDetails::palletId).toList());
        return new PalletPage(page.pallets().stream()
                .map(pallet -> pallet.withCarrier(carriers.get(pallet.palletId())))
                .toList(), page.page(), page.size(), page.totalElements());
    }

    public boolean hasCarrier(final PalletId palletId) {
        return this.palletTransportRepository.findCurrent(palletId).isPresent();
    }

    public PalletDetails assign(final PalletDetails pallet, final PalletCarrier carrier) {
        return pallet.withCarrier(this.palletTransportRepository.append(pallet.palletId(), pallet.trackingNumber(), carrier));
    }

    public List<PalletCarrier> history(final PalletId palletId) {
        return this.palletTransportRepository.findHistory(palletId);
    }
}
