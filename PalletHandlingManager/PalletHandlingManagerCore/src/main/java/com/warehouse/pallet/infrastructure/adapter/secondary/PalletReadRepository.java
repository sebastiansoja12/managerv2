package com.warehouse.pallet.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.pallet.infrastructure.adapter.secondary.entity.PalletEntity;

@Repository
public interface PalletReadRepository extends JpaRepository<PalletEntity, String> {
}
