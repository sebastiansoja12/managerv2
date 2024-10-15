package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.MailMapper;
import com.warehouse.mail.domain.port.primary.MailPort;

import com.warehouse.tools.mail.MailProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final MailProperty mailProperty;

    private final MailMapper mapper = getMapper(MailMapper.class);

    @Override
    public void sendNotification(Shipment shipment) {
        final com.warehouse.mail.domain.vo.Notification mailNotification = mapper.map(shipment, mailProperty);
        mailPort.sendNotification(mailNotification);
    }
}
