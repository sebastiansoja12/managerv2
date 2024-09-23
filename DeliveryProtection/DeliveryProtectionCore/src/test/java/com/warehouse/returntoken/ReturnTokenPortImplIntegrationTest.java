package com.warehouse.returntoken;


import com.warehouse.commonassets.enumeration.ParcelStatus;
import com.warehouse.returntoken.configuration.ReturnTokenTestConfiguration;
import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.vo.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ReturnTokenTestConfiguration.class)
public class ReturnTokenPortImplIntegrationTest {

    @Autowired
    private ReturnTokenPort returnTokenPort;

    @Test
    void shouldDetermineReturnToken() {
        // given
        final Parcel parcel = new Parcel(1L, null, ParcelStatus.RETURN, false);
        // when
        final Token token = returnTokenPort.determine(parcel);
        // then
        assertEquals("7519FF", token.getValue());
        assertThat(token.getValue()).isInstanceOf(String.class);
    }

    @Test
    void shouldNotDetermineTokenWhenParcelIsLocked() {
        // given
        final Parcel parcel = new Parcel(1L, null, ParcelStatus.RETURN, true);
        // when
        final Token token = returnTokenPort.determine(parcel);
        // then
        assertNull(token.getValue());
    }
}
