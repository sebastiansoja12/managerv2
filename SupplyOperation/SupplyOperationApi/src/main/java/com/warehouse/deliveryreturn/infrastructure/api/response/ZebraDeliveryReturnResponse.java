package com.warehouse.deliveryreturn.infrastructure.api.response;


import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "ZebraDeliveryReturnResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeliveryReturnResponse {

	public ZebraDeliveryReturnResponse(List<DeliveryReturnResponseDetail> deliveryReturnResponses, String supplierCode,
			String depotCode) {
		this.deliveryReturnResponses = deliveryReturnResponses;
		this.supplierCode = supplierCode;
		this.depotCode = depotCode;
	}

    public ZebraDeliveryReturnResponse() {
    }

    @XmlElementWrapper(name = "DeliveryReturnResponses")
	@XmlElement(name = "DeliveryReturnResponse")
    private List<DeliveryReturnResponseDetail> deliveryReturnResponses;

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DepotCode")
    private String depotCode;

    public List<DeliveryReturnResponseDetail> getDeliveryReturnResponses() {
        return deliveryReturnResponses;
    }

    public void setDeliveryReturnResponses(List<DeliveryReturnResponseDetail> deliveryReturnResponses) {
        this.deliveryReturnResponses = deliveryReturnResponses;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }
}
