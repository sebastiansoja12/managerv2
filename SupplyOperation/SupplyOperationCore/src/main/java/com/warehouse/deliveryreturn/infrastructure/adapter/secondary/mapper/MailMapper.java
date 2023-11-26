package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.MailProperty;
import com.warehouse.mail.domain.vo.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface MailMapper {
    default Notification map(Parcel parcel, MailProperty mailProperty) {
        return Notification.builder()
                .body(String.format(mailProperty.getMessage(), parcel.getId(), parcel.getSenderEmail()))
                .recipient(parcel.getRecipientEmail())
                .subject(mailProperty.getSubject())
                .build();
    }
}
