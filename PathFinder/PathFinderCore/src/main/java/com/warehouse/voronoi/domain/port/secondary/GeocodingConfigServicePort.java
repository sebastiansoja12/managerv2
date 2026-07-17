package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;

public interface GeocodingConfigServicePort {
    GeocodingConfig findGeocodingConfig(final GeocodingProvider provider);
}
