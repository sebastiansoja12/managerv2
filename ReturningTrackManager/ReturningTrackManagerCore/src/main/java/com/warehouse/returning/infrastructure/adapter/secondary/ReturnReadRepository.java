package com.warehouse.returning.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;

@Repository
public interface ReturnReadRepository extends JpaRepository<ReturnEntity, Long> {

    Optional<ReturnEntity> findFirstByParcelId(Long parcelId);
    Optional<ReturnEntity> findFirstByParcelIdAndReturnToken(Long parcelId, String returnToken);
}
