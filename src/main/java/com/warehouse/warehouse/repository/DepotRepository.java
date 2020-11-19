package com.warehouse.warehouse.repository;

import com.warehouse.warehouse.model.Depot;
import com.warehouse.warehouse.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepotRepository extends JpaRepository<Depot,Long> {
    List<Depot> findAll();

    List<Depot> findByParcelId(final UUID id);


    List<Depot> findByParcel_ParcelCode(final String parcelCode);

}