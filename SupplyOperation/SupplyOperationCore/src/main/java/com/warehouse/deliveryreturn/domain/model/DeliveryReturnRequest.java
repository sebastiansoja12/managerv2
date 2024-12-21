package com.warehouse.deliveryreturn.domain.model;


import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.vo.DeviceInformation;


public class DeliveryReturnRequest {
    private ProcessType processType;
    private DeviceInformation deviceInformation;
    private List<DeliveryReturnDetails> deliveryReturnDetails;

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryReturnDetails> getDeliveryReturnDetails() {
        return deliveryReturnDetails;
    }

    public void setDeliveryReturnDetails(final List<DeliveryReturnDetails> deliveryReturnDetails) {
        this.deliveryReturnDetails = deliveryReturnDetails;
    }

    public DeliveryReturnRequest(final ProcessType processType,
                                 final DeviceInformation deviceInformation,
                                 final List<DeliveryReturnDetails> deliveryReturnDetails) {
        this.processType = processType;
        this.deviceInformation = deviceInformation;
        this.deliveryReturnDetails = deliveryReturnDetails;
    }

    public void validateStatuses() {
        deliveryReturnDetails.forEach(DeliveryReturnDetails::validateDeliveryStatus);
    }

    public boolean isReturnProcessType() {
        return RETURN.equals(processType);
    }

    public void rewriteSupplierCodeFromDevice() {
        deliveryReturnDetails = deliveryReturnDetails
                .stream()
				.peek(deliveryReturnDetail -> deliveryReturnDetail
						.setSupplierCode(new SupplierCode(deviceInformation.getUsername())))
                .collect(Collectors.toList());
    }

    public void rewriteDepartmentCodeFromDevice() {
        deliveryReturnDetails = deliveryReturnDetails
                .stream()
                .peek(deliveryReturnDetail -> deliveryReturnDetail
                        .setDepartmentCode(deviceInformation.getDepartmentCode()))
                .collect(Collectors.toList());
    }

    public String getDepotCode() {
        return deviceInformation != null ? deviceInformation.getDepartmentCode().getValue() : StringUtils.EMPTY;
    }
}
