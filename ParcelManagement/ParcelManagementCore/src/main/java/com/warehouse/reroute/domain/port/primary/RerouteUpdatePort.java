package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;

public interface RerouteUpdatePort {
    RerouteParcelResponse update(RerouteParcelRequest request);

}
