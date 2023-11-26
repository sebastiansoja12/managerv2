package com.warehouse.qrcode.infrastructure.adapter.secondary;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.qrcode.infrastructure.adapter.secondary.entity.ParcelEntity;


@Repository("qrCode.parcelReadRepository")
public interface ParcelReadRepository extends JpaRepository<ParcelEntity, Long> {
}
