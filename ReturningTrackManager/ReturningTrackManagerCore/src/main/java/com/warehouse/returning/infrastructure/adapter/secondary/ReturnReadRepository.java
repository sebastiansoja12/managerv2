package com.warehouse.returning.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;

@Repository
public interface ReturnReadRepository extends JpaRepository<ReturnPackageEntity, ReturnId> {

}
