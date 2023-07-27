package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.reroute.domain.model.City;
import com.warehouse.reroute.domain.model.Parcel;

public interface PathFinderServicePort {
    City determineNewDeliveryDepot(Parcel parcel);
}
