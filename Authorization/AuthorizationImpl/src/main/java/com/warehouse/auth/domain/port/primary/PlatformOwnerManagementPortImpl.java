package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.CreatePlatformOwnerCommand;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRepository;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class PlatformOwnerManagementPortImpl implements PlatformOwnerManagementPort {

    private final PlatformOwnerRepository platformOwnerRepository;

    private final PlatformOwnerRefreshTokenRepository platformOwnerRefreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public PlatformOwnerManagementPortImpl(final PlatformOwnerRepository platformOwnerRepository,
                                           final PlatformOwnerRefreshTokenRepository platformOwnerRefreshTokenRepository,
                                           final PasswordEncoder passwordEncoder) {
        this.platformOwnerRepository = platformOwnerRepository;
        this.platformOwnerRefreshTokenRepository = platformOwnerRefreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<PlatformOwner> findAll() {
        return platformOwnerRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(PlatformOwner::getUsername))
                .toList();
    }

    @Override
    public PlatformOwnerId create(final CreatePlatformOwnerCommand command) {
        if (StringUtils.isAnyBlank(command.username(), command.password(), command.firstName(), command.lastName(),
                command.email())) {
            throw new IllegalArgumentException("All platform owner fields are required");
        }
        if (platformOwnerRepository.existsByUsernameOrEmail(command.username(), command.email())) {
            throw new IllegalArgumentException("Platform owner with given username or email already exists");
        }
        final PlatformOwnerId platformOwnerId = platformOwnerRepository.nextPlatformOwnerId();
        platformOwnerRepository.save(PlatformOwner.create(platformOwnerId, command.username(),
                passwordEncoder.encode(command.password()), command.firstName(), command.lastName(), command.email()));
        log.info("Platform owner {} created", platformOwnerId.value());
        return platformOwnerId;
    }

    @Override
    public void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId) {
        if (platformOwnerId.equals(actingPlatformOwnerId)) {
            throw new IllegalStateException("Platform owner cannot block own account");
        }
        final PlatformOwner platformOwner = find(platformOwnerId);
        if (platformOwner.isActive() && platformOwnerRepository.countActive() <= 1) {
            throw new IllegalStateException("At least one active platform owner is required");
        }
        platformOwnerRepository.save(platformOwner.block());
        platformOwnerRefreshTokenRepository.deleteAll(platformOwnerId);
        log.info("Platform owner {} blocked by {}", platformOwnerId.value(), actingPlatformOwnerId.value());
    }

    @Override
    public void unblock(final PlatformOwnerId platformOwnerId) {
        platformOwnerRepository.save(find(platformOwnerId).unblock());
        log.info("Platform owner {} unblocked", platformOwnerId.value());
    }

    @Override
    public void changePassword(final PlatformOwnerId platformOwnerId, final String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password is required");
        }
        platformOwnerRepository.save(find(platformOwnerId).changePassword(passwordEncoder.encode(password)));
        platformOwnerRefreshTokenRepository.deleteAll(platformOwnerId);
        log.info("Password changed for platform owner {}", platformOwnerId.value());
    }

    private PlatformOwner find(final PlatformOwnerId platformOwnerId) {
        return platformOwnerRepository.findById(platformOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("Platform owner " + platformOwnerId.value() + " does not exist"));
    }
}
