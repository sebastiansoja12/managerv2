package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.vo.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {

    com.warehouse.mail.domain.vo.Notification map(Notification notification);
}
