package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public record ProcessCommunication(ProcessId processId,
                                   ProcessType processType,
                                   ServiceType targetService,
                                   String request,
                                   String response,
                                   String faultDescription) {
}
