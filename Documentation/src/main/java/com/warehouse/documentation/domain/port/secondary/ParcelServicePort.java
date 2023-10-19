package com.warehouse.documentation.domain.port.secondary;

import com.warehouse.documentation.domain.model.Parcel;

public interface ParcelServicePort {
    Parcel pullParcel(Long id);
}
