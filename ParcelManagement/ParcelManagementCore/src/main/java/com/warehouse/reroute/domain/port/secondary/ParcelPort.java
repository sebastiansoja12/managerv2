package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;

public interface ParcelPort {

    ParcelUpdateResponse update(UpdateParcelRequest request);

}
