package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.model.RerouteNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RequestMapper {

    @Mapping(source = "parcelId", target = "value")
    ParcelId map(RerouteRequest rerouteRequest);

    Notification map(RerouteNotification rerouteNotification);

}
