package com.warehouse.qrcode.infrastructure.adapter.secondary;


import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.qrcode.infrastructure.adapter.secondary.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("qrCode.shipmentReadRepository")
public interface ShipmentReadRepository extends JpaRepository<ShipmentEntity, ShipmentId> {
}
