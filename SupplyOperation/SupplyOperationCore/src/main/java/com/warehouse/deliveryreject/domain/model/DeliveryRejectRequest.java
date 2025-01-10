package com.warehouse.deliveryreject.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.terminal.enumeration.ExecutionSourceType;
import com.warehouse.terminal.information.ExecutionSourceResolver;

public class DeliveryRejectRequest implements Serializable, ExecutionSourceResolver {
    private List<DeliveryRejectDetails> deliveryRejectDetails;
    private DeviceInformation deviceInformation;
    private ProcessType processType;

    public DeliveryRejectRequest(final List<DeliveryRejectDetails> deliveryRejectDetails,
                                 final DeviceInformation deviceInformation,
                                 final ProcessType processType) {
        this.deliveryRejectDetails = deliveryRejectDetails;
        this.deviceInformation = deviceInformation;
        this.processType = processType;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setDeliveryRejectDetails(final List<DeliveryRejectDetails> deliveryRejectDetails) {
        this.deliveryRejectDetails = deliveryRejectDetails;
    }

    public void setDeviceInformation(final DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryRejectDetails> getDeliveryRejectDetails() {
        return deliveryRejectDetails;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void rewriteSupplierCodeFromDevice() {
        deliveryRejectDetails = deliveryRejectDetails
                .stream()
                .peek(deliveryReturnDetail -> deliveryReturnDetail
                        .setSupplierCode(new SupplierCode(deviceInformation.getUsername())))
                .collect(Collectors.toList());
    }

    public void rewriteDepartmentCodeFromDevice() {
        deliveryRejectDetails = deliveryRejectDetails
                .stream()
                .peek(deliveryReturnDetail -> deliveryReturnDetail
                        .setDepartmentCode(deviceInformation.getDepartmentCode()))
                .collect(Collectors.toList());
    }

    public String getDepartmentCode() {
        return deviceInformation != null ? deviceInformation.getDepartmentCode().getValue() : StringUtils.EMPTY;
    }

    public void validateStatuses() {
        deliveryRejectDetails.forEach(DeliveryRejectDetails::validateDeliveryStatus);
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }
}
