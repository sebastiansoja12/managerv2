package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.reroute.domain.model.Notification;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface NotificationMapper {

    com.warehouse.mail.domain.vo.Notification map(Notification notification);
}
