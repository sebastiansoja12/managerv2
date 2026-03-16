package com.warehouse.dangerousgood.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity.DangerousGoodEntity;

@Repository("dangerousGood.dangerousGoodReadRepository")
public interface DangerousGoodReadRepository extends JpaRepository<DangerousGoodEntity, DangerousGoodId> {
}
