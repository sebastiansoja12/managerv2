package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.asyncjob.infrastructure.adapter.secondary.entity.AsyncJobEntity;

@Repository
public interface AsyncJobJpaRepository extends JpaRepository<AsyncJobEntity, UUID> {
}
