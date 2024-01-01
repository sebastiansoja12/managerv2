package com.warehouse.exceptioncatcher.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.entity.ExceptionEntity;

@Repository
public interface ExceptionReadRepository extends JpaRepository<ExceptionEntity, Long> {
}
