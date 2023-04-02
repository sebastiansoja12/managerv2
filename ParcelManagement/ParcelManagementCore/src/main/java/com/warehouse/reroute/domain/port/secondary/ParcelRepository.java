package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;

public interface ParcelRepository {

    ParcelUpdateResponse loadByParcelId(Long parcelId);

}
