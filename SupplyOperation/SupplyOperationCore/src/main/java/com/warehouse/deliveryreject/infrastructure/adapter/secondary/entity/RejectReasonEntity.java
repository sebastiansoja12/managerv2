package com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reject_reason")
public class RejectReasonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason")
    private String reason;

    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "reject_department_code")
    private String rejectDepartmentCode;

    public RejectReasonEntity() {
    }

    public RejectReasonEntity(final Long id, final String reason, final Long shipmentId, final String recipient,
                              final String rejectDepartmentCode) {
        this.id = id;
        this.reason = reason;
        this.shipmentId = shipmentId;
        this.recipient = recipient;
        this.rejectDepartmentCode = rejectDepartmentCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }

    public String getRejectDepartmentCode() {
        return rejectDepartmentCode;
    }

    public void setRejectDepartmentCode(final String rejectDepartmentCode) {
        this.rejectDepartmentCode = rejectDepartmentCode;
    }
}
