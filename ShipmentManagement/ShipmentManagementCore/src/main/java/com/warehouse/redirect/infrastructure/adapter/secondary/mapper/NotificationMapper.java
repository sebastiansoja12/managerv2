package com.warehouse.redirect.infrastructure.adapter.secondary.mapper;

import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.redirect.domain.vo.RedirectNotification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {
    Notification map(RedirectNotification redirectNotification);
}
