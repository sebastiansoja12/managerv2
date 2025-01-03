package com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.model.DeliveryReject;

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
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "reject_department_code")
    @AttributeOverride(name = "value", column = @Column(name = "reject_department_code"))
    private DepartmentCode rejectDepartmentCode;

    @Column(name = "supplier_code")
    @AttributeOverride(name = "value", column = @Column(name = "supplier_code"))
    private SupplierCode supplierCode;

    @Column(name = "delivery_status")
    private String deliveryStatus;


    public RejectReasonEntity() {
    }

    public RejectReasonEntity(final Long id, final String reason,
                              final ShipmentId shipmentId,
                              final String recipient,
                              final DepartmentCode rejectDepartmentCode,
                              final SupplierCode supplierCode,
                              final String deliveryStatus) {
        this.id = id;
        this.reason = reason;
        this.shipmentId = shipmentId;
        this.recipient = recipient;
        this.rejectDepartmentCode = rejectDepartmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
    }

    public static RejectReasonEntity from(final DeliveryReject deliveryReject) {
        return new RejectReasonEntity(null, deliveryReject.getRejectReason().getValue(),
                deliveryReject.getShipmentId(), deliveryReject.getRecipient().toString(),
                deliveryReject.getDepartmentCode(),
                deliveryReject.getSupplierCode(),
                deliveryReject.getDeliveryStatus().toString());
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }

    public DepartmentCode getRejectDepartmentCode() {
        return rejectDepartmentCode;
    }

    public void setRejectDepartmentCode(final DepartmentCode rejectDepartmentCode) {
        this.rejectDepartmentCode = rejectDepartmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }
}
