package com.warehouse.routetracker.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;

public class TerminalIdInformation {
    private ProcessType processType;
    private ShipmentId shipmentId;
    private TerminalId terminalId;

	public TerminalIdInformation(final ProcessType processType, final ShipmentId shipmentId,
			final TerminalId terminalId) {
        this.processType = processType;
        this.shipmentId = shipmentId;
        this.terminalId = terminalId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public TerminalId getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(final TerminalId terminalId) {
        this.terminalId = terminalId;
    }
}
