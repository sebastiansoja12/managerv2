package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@XmlRootElement(name = "ProcessReturn")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessReturnDto {

    @XmlElement(name = "ReturnID")
    Long returnId;

    @XmlElement(name = "ProcessStatus")
    String processStatus;
}
