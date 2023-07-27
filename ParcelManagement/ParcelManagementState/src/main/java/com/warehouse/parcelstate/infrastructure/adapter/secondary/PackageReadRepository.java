package com.warehouse.parcelstate.infrastructure.adapter.secondary;


import com.warehouse.parcelstate.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PackageReadRepository extends JpaRepository<ParcelEntity, Long> {

    Optional<ParcelEntity> findParcelEntityById(Long id);


    ParcelEntity findByFirstName(String firstName);
}
