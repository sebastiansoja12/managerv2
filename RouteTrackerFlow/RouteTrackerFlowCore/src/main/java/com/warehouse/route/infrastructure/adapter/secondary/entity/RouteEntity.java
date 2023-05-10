package com.warehouse.route.infrastructure.adapter.secondary.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "route")
@NamedEntityGraph(name = "RouteEntity.full", attributeNodes = {
        @NamedAttributeNode("user"),
        @NamedAttributeNode("parcel"),
        @NamedAttributeNode("depot"),
        @NamedAttributeNode("supplier")
})
public class RouteEntity {

    @Id
    @GeneratedValue()
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false)
    private UUID id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "created", nullable = false)
    private LocalDateTime created;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Column(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", referencedColumnName = "id")
    @Column(name = "parcel_id", nullable = false)
    private ParcelEntity parcel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_id", referencedColumnName = "id")
    @Column(name = "depot_id")
    private DepotEntity depot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @Column(name = "supplier_id")
    private SupplierEntity supplier;
}

