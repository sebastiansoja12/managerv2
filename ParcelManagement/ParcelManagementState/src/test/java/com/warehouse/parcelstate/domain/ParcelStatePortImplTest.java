package com.warehouse.parcelstate.domain;

import com.warehouse.parcelstate.domain.model.RerouteParcel;
import com.warehouse.parcelstate.domain.port.primary.ParcelStatePortImpl;
import com.warehouse.parcelstate.domain.port.secondary.ParcelStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParcelStatePortImplTest {

    @Mock
    private ParcelStatePortImpl parcelStatePort;

    @Mock
    private ParcelStateRepository parcelStateRepository;
    @BeforeEach
    void setup() {
        parcelStatePort = new ParcelStatePortImpl(parcelStateRepository);
    }

    @Test
    void shouldRerouteParcel() {
        // given
        final RerouteParcel rerouteParcel = buildRerouteParcel();
        // when

        // then
    }

    private RerouteParcel buildRerouteParcel() {
        return null;
    }
}
