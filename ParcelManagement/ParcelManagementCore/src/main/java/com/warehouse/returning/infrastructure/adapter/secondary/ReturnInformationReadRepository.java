package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInformationReadRepository extends JpaRepository<ReturnInformationEntity, Long> {

}
