package com.warehouse.reroute.domain.model;


import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParcelTest {

    @Test
    void shouldSuccessfullyBuildParcel() {
        // given && when
        final Parcel parcel = Parcel.builder()
                .sender(sender())
                .recipient(recipient())
                .parcelType(parcelType())
                .build();
        // then
        assertThat(parcel.getSender().getFirstName()).isNull();
    }

    public Recipient recipient() {
        return Recipient.builder().build();
    }
    public Sender sender() {
        return Sender.builder().build();
    }
    public ParcelType parcelType() {
        return ParcelType.AVERAGE;
    }
}
