package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.MailMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.MailProperty;
import com.warehouse.mail.domain.port.primary.MailPort;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final MailProperty mailProperty;

    private final MailMapper mapper = getMapper(MailMapper.class);

    @Override
    public void sendNotification(Parcel parcel) {
        final com.warehouse.mail.domain.vo.Notification mailNotification = mapper.map(parcel, mailProperty);
        mailPort.sendNotification(mailNotification);
    }
}
