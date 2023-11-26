package com.warehouse.deliveryreturn.infrastructure.api.response;

import java.util.UUID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "DeliveryReturnResponseDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryReturnResponseDetail {

    @XmlElement(name = "DeliveryID")
    private UUID id;
    @XmlElement(name = "ParcelID")
    private Long parcelId;
    @XmlElement(name = "DeliveryStatus")
    private String deliveryStatus;
    @XmlElement(name = "ReturnToken")
    private String returnToken;
    @XmlElement(name = "UpdateStatus")
    private UpdateStatus updateStatus;
}
