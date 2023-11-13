package com.warehouse.returning.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnInformationEntity;

@Repository
public interface ReturnInformationReadRepository extends JpaRepository<ReturnInformationEntity, Long> {

}
