package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.PalletPlacementId;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletPlacementEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PalletPlacementReadRepository extends JpaRepository<PalletPlacementEntity, PalletPlacementId> {

    Optional<PalletPlacementEntity> findByOperatorIdValueAndPalletTrackingNumberValue(final Long operatorId,
                                                                                     final String palletTrackingNumber);

    List<PalletPlacementEntity> findByOperatorIdValueAndPalletTrackingNumberValueContainingOrderByPalletTrackingNumberValueAsc(
            final Long operatorId, final String fragment, final Pageable pageable);
}
