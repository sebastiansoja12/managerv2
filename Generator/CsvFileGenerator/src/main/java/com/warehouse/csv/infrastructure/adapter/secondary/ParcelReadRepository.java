package com.warehouse.csv.infrastructure.adapter.secondary;


import com.warehouse.csv.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("csv.parcelReadRepository")
public interface ParcelReadRepository extends JpaRepository<ParcelEntity, Long> {
}
