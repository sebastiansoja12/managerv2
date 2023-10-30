package com.warehouse.zebra.infrastructure.api.requestmodel;


import com.warehouse.zebra.infrastructure.api.dto.ParcelDto;
import com.warehouse.zebra.infrastructure.api.dto.ReturnStatusDto;
import com.warehouse.zebra.infrastructure.api.dto.SupplierCodeDto;
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
    private ParcelDto parcel;

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "ReturnStatus")
    private ReturnStatusDto returnStatus;

    @XmlElement(name = "ReturnToken")
    private String returnToken;

    @XmlElement(name = "SupplierCode")
    private SupplierCodeDto supplierCode;
}
