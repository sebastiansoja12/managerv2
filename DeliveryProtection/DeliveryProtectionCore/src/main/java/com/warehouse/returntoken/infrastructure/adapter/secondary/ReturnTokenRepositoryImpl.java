package com.warehouse.returntoken.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.port.secondary.ReturnTokenRepository;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;
import com.warehouse.returntoken.infrastructure.adapter.secondary.mapper.ReturnTokenToEntityMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenRepositoryImpl implements ReturnTokenRepository {

    private final ReturnTokenReadRepository repository;

    private final ReturnTokenToEntityMapper toEntityMapper = getMapper(ReturnTokenToEntityMapper.class);

    @Override
    public Optional<ReturnTokenEntity> findByShipmentId(final ShipmentId shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }

    @Override
    public Boolean exists(final String value) {
        return repository.existsByToken(value);
    }

    @Override
    public void create(final ReturnToken returnToken) {
        final ReturnTokenEntity entity = toEntityMapper.map(returnToken);
        this.repository.save(entity);
    }
}
