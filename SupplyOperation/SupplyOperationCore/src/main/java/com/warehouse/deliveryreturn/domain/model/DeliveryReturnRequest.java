package com.warehouse.deliveryreturn.domain.model;




import java.util.List;
import java.util.stream.Collectors;


import com.warehouse.commonassets.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.warehouse.commonassets.ProcessType.RETURN;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryReturnRequest {
    private ProcessType processType;
    private DeviceInformation deviceInformation;
    private List<DeliveryReturnDetails> deliveryReturnDetails;

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
						.setSupplierCode(deviceInformation.getUsername()))
                .collect(Collectors.toList());
    }

    public void rewriteDepotCodeFromDevice() {
        deliveryReturnDetails = deliveryReturnDetails
                .stream()
                .peek(deliveryReturnDetail -> deliveryReturnDetail
                        .setDepotCode(deviceInformation.getDepotCode()))
                .collect(Collectors.toList());
    }

    public String getDepotCode() {
        return deviceInformation != null ? deviceInformation.getDepotCode() : StringUtils.EMPTY;
    }
}
