package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnReadRepository extends JpaRepository<ReturnEntity, Long> {
}
