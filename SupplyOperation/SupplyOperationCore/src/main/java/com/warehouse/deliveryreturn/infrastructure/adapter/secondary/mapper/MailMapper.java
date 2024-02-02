package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.tools.mail.MailProperty;
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
