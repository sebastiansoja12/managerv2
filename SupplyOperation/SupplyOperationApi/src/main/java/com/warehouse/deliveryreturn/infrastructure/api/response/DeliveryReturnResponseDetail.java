package com.warehouse.deliveryreturn.infrastructure.api.response;

import java.util.UUID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "DeliveryReturnResponseDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryReturnResponseDetail {
	public DeliveryReturnResponseDetail(UUID id, Long parcelId, String deliveryStatus, String returnToken,
			UpdateStatus updateStatus) {
		this.id = id;
		this.parcelId = parcelId;
		this.deliveryStatus = deliveryStatus;
		this.returnToken = returnToken;
		this.updateStatus = updateStatus;
	}

    public DeliveryReturnResponseDetail() {
    }

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long parcelId) {
        this.parcelId = parcelId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(String returnToken) {
        this.returnToken = returnToken;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
