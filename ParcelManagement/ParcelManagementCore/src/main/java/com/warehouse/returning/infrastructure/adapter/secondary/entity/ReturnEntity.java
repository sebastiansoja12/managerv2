package com.warehouse.returning.infrastructure.adapter.secondary.entity;


import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "returning")
public class ReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parcel_id", nullable = false)
    private Long parcelId;

    @Column(name = "return_status", nullable = false)
    private ReturnStatus returnStatus;

    @Column(name = "return_token", nullable = false)
    private String returnToken;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "returning_information_id", referencedColumnName = "id")
    private ReturnInformationEntity returnInformationEntity;

    public void attachReturnInformation(ReturnInformationEntity returnInformationEntity) {
        this.returnInformationEntity = returnInformationEntity;
    }

    public void completeReturn() {
        this.returnStatus = ReturnStatus.COMPLETED;
    }
}
