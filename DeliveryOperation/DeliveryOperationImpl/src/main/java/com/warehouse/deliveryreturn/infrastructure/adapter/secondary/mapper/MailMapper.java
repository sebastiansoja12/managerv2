package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import com.warehouse.deliveryreturn.domain.vo.DeliverableShipment;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.tools.mail.MailProperty;
import org.mapstruct.Mapper;

@Mapper
public interface MailMapper {
    default Notification map(DeliverableShipment deliverableShipment, MailProperty mailProperty) {
        return Notification.builder()
                .body(String.format(mailProperty.getMessage(), deliverableShipment.getShipmentId(), deliverableShipment.getSenderEmail()))
                .recipient(deliverableShipment.getRecipientEmail())
                .subject(mailProperty.getSubject())
                .build();
    }
}
