package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.tools.mail.MailProperty;
import org.mapstruct.Mapper;

@Mapper
public interface MailMapper {
    default Notification map(Shipment shipment, MailProperty mailProperty) {
        return Notification.builder()
                .body(String.format(mailProperty.getMessage(), shipment.getShipmentId(), shipment.getSenderEmail()))
                .recipient(shipment.getRecipientEmail())
                .subject(mailProperty.getSubject())
                .build();
    }
}
