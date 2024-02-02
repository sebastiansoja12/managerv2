package com.warehouse.zebra.infrastructure.api.requestmodel;


import java.util.List;

import com.warehouse.zebrainitialize.model.ParcelCreatedRequest;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlRootElement(name = "ZebraRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZebraRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    @XmlElement(name = "ZebraDeviceInformation")
    private ZebraDeviceInformation zebraDeviceInformation;

    @XmlElementWrapper(name = "ReturnRequestInformations")
    @XmlElement(name = "ReturnRequestInformation")
    private List<ReturnRequestInformation> requests;

    @XmlElementWrapper(name = "ParcelCreatedRequests")
    @XmlElement(name = "ParcelCreatedRequest")
    private List<ParcelCreatedRequest> parcelCreatedRequests;
}
