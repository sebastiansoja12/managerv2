package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.voronoi.infrastructure.adapter.secondary.entity.PositionStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionStackReadRepository extends JpaRepository<PositionStackEntity, Long> {
}
