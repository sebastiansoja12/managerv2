package com.warehouse.departmentmap.domain.vo;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.Optional;

public record LocationReservation(PalletTrackingNumber palletTrackingNumber,
                                  String note,
                                  UserId reservedBy,
                                  Instant reservedAt) {

    public Optional<PalletTrackingNumber> reservedPallet() {
        return Optional.ofNullable(palletTrackingNumber);
    }

    public boolean isFor(final PalletTrackingNumber trackingNumber) {
        return palletTrackingNumber != null && palletTrackingNumber.equals(trackingNumber);
    }
}
