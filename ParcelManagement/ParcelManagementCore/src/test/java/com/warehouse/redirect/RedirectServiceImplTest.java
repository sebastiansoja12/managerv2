package com.warehouse.redirect;

import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import com.warehouse.redirect.domain.service.RedirectServiceImpl;
import com.warehouse.redirect.domain.vo.RedirectToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RedirectServiceImplTest {

    @Mock
    private RedirectTokenRepository redirectTokenRepository;

    private RedirectServiceImpl redirectService;

    @BeforeEach
    void setup() {
        redirectService = new RedirectServiceImpl(redirectTokenRepository);
    }

    @Test
    void shouldSaveToken() {
        // given
        final RedirectToken redirectToken = new RedirectToken();
        // when
        redirectService.saveRedirectToken(redirectToken);
        // then
        verify(redirectTokenRepository, times(1)).save(redirectToken);
    }
}
