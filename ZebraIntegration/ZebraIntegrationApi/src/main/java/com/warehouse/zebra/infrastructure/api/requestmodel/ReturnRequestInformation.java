package com.warehouse.zebra.infrastructure.api.requestmodel;


import com.warehouse.zebra.infrastructure.api.dto.Parcel;
import com.warehouse.zebra.infrastructure.api.dto.ReturnStatus;
import com.warehouse.zebra.infrastructure.api.dto.SupplierCode;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "ReturnRequestInformation")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequestInformation {

    @XmlElement(name = "Parcel")
    private Parcel parcel;

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "ReturnStatus")
    private ReturnStatus returnStatus;

    @XmlElement(name = "ReturnToken")
    private String returnToken;

    @XmlElement(name = "SupplierCode")
    private SupplierCode supplierCode;
}
