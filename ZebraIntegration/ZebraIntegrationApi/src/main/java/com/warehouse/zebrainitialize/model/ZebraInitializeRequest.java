package com.warehouse.zebrainitialize.model;

import java.util.List;

import com.warehouse.zebra.infrastructure.api.requestmodel.ProcessType;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraDeviceInformation;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "ZebraInitializeRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZebraInitializeRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    @XmlElement(name = "ZebraDeviceInformation")
    private ZebraDeviceInformation zebraDeviceInformation;

    @XmlElementWrapper(name = "ParcelCreatedRequests")
    @XmlElement(name = "ParcelCreatedRequest")
    private List<ParcelCreatedRequest> parcelCreatedRequests;

}
