package com.warehouse.routetracker.infrastructure.adapter.secondary.mapper;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {
    com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status map(ParcelStatus parcelStatus);
}
