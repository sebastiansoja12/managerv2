package com.warehouse.auth.domain.listener;

import com.warehouse.auth.domain.event.UserCreatedEvent;
import com.warehouse.auth.domain.event.UserLoggedOutEvent;
import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.service.RefreshTokenService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.UserSnapshot;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserListener {

    private final MailServicePort mailServicePort;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    public UserListener(final MailServicePort mailServicePort,
                        final RefreshTokenService refreshTokenService,
                        final UserService userService) {
        this.mailServicePort = mailServicePort;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @EventListener
    public void handle(final UserCreatedEvent event) {

    }

    @EventListener
    public void handle(final UserLoggedOutEvent event) {
        final UserSnapshot snapshot = event.getSnapshot();
        final UserId userId = snapshot.userId();
        final RefreshToken refreshToken = refreshTokenService.findTokenByUserId(userId);
        refreshTokenService.deleteRefreshToken(snapshot.userId(), refreshToken.getToken());
    }

}
