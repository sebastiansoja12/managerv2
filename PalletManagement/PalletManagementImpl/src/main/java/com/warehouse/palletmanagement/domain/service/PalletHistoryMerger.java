package com.warehouse.palletmanagement.domain.service;

import com.warehouse.palletmanagement.domain.enumeration.PalletHistoryEventType;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class PalletHistoryMerger {

    private PalletHistoryMerger() {
    }

    public static List<PalletHistoryEntry> merge(final List<PalletHistoryEntry> palletHistory,
                                                 final List<PalletCarrier> transportHistory) {
        final Stream<PalletHistoryEntry> transportEntries = transportHistory.stream()
                .map(carrier -> new PalletHistoryEntry(PalletHistoryEventType.CARRIER_ASSIGNED, null,
                        carrier.assignedAt(), null, carrier.assignedBy(), null, PalletTransportPolicy.describe(carrier)));
        return Stream.concat(palletHistory.stream(), transportEntries)
                .sorted(Comparator.comparing(PalletHistoryEntry::occurredAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
