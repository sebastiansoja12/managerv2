package com.warehouse.deliverymissed.domain.port.secondary;


import com.warehouse.deliverymissed.domain.vo.UpdateStatusParcelRequest;

public interface ParcelStatusServicePort {
    void updateParcelStatus(UpdateStatusParcelRequest updateStatusParcelRequest);
}
