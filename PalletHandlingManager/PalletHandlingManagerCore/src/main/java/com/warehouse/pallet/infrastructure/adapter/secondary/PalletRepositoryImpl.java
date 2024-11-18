package com.warehouse.pallet.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.vo.PalletId;
import com.warehouse.pallet.infrastructure.adapter.secondary.entity.PalletEntity;
import com.warehouse.pallet.infrastructure.adapter.secondary.mapper.EntityMapper;
import com.warehouse.pallet.infrastructure.adapter.secondary.mapper.ModelMapper;

public class PalletRepositoryImpl implements PalletRepository {

    private final PalletReadRepository repository;

    private final EntityMapper entityMapper = getMapper(EntityMapper.class);

    private final ModelMapper modelMapper = getMapper(ModelMapper.class);

    public PalletRepositoryImpl(final PalletReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createOrUpdate(final Pallet pallet) {
        final PalletEntity palletEntity = entityMapper.map(pallet);
        this.repository.save(palletEntity);
    }

    @Override
    public Pallet findById(final PalletId palletId) {
        return this.repository
                .findById(palletId.value())
                .map(modelMapper::map)
                .orElse(null);
    }
}
