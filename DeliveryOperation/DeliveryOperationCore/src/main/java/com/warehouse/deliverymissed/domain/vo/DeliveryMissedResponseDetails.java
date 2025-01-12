package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

public class DeliveryMissedResponseDetails {
    private final ShipmentId shipmentId;
    private final DepartmentCode departmentCode;
    private final SupplierCode supplierCode;
    private final PenaltyFee penaltyFee;
    private final SuggestedAction suggestedAction;
    private final Recipient recipient;

    public DeliveryMissedResponseDetails(final ShipmentId shipmentId,
                                         final DepartmentCode departmentCode,
                                         final SupplierCode supplierCode,
                                         final PenaltyFee penaltyFee,
                                         final SuggestedAction suggestedAction,
                                         final Recipient recipient) {
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.penaltyFee = penaltyFee;
        this.suggestedAction = suggestedAction;
        this.recipient = recipient;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public SuggestedAction getSuggestedAction() {
        return suggestedAction;
    }

    public PenaltyFee getPenaltyFee() {
        return penaltyFee;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
}
