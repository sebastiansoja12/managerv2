package com.warehouse.qrcode.domain.port.secondary;

import com.warehouse.qrcode.domain.model.Parcel;

public interface ParcelRepository {
    Parcel find(Long id);
}
