package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Slf4j
@AllArgsConstructor
public class RerouteTokenRepositoryImpl implements RerouteTokenRepository {

    private final RerouteTokenMapper rerouteTokenMapper;

    private final RerouteTokenReadRepository repository;

    private final Long EXPIRY_TIME = 600L;


    @Override
    public RerouteToken loadByTokenAndParcelId(Token token, ParcelId aParcelId) {
        return repository.loadByTokenAndParcelId(token.getValue(), aParcelId.getValue())
                .map(rerouteTokenMapper::map).orElseThrow( () -> new RerouteTokenNotFoundException(
                        "Reroute token was not found"
                ));
    }

    @Override
    public RerouteToken findByToken(Token token) {
        return repository.findByToken(token.getValue()).map(rerouteTokenMapper::map).orElseThrow(
                () -> new RerouteTokenNotFoundException("Reroute token was not found"));
    }

    @Override
    public Integer saveReroutingToken(Long id) {
        final RerouteTokenEntity entity = repository.save(generateRerouteToken(id));

        return entity.getToken();
    }

    @Override
    public void deleteByToken(Token token) {
        repository.deleteByToken(token.getValue());
    }

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        log.warn("Cleanup database from expired reroute tokens");
        repository.deleteAllExpiredSince(Instant.now());
        log.info("Expired reroute tokens have been successfully deleted");
    }

    private RerouteTokenEntity generateRerouteToken(Long parcelId) {
        final RerouteToken rerouteToken = new RerouteToken();
        rerouteToken.setToken(rerouteToken.generateToken());
        rerouteToken.setCreatedDate(Instant.now());
        rerouteToken.setExpiryDate(Instant.now().plusSeconds(EXPIRY_TIME));
        rerouteToken.setParcelId(parcelId);
        return rerouteTokenMapper.map(rerouteToken);
    }


}
