package com.warehouse.redirect;

import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.service.RedirectTokenGeneratorImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RedirectTokenGeneratorTest {

    private final RedirectTokenGenerator generator = new RedirectTokenGeneratorImpl();


    @Test
    void shouldGenerateRedirectToken() {
        // given
        final String email = "sebastian5152@wp.pl";
        final Long parcelId = 1L;
        // when
        final String token = generator.generateToken(parcelId, email);
        // then
        assertThat(token.length()).isEqualTo(8);
    }
}
