package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import com.warehouse.shipment.infrastructure.adapter.secondary.entity.aud.ShipmentRevisionEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_envers_info")
@RevisionEntity(ShipmentRevisionEntityListener.class)
public class AuditEnversInfo extends DefaultRevisionEntity {
	@Column(name = "username")
	private String username;
}