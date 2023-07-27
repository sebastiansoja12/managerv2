package com.warehouse.shipment.domain.exception;

public class DestinationDepotDeterminationException extends RuntimeException {
    public DestinationDepotDeterminationException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public DestinationDepotDeterminationException(String exMessage) {
        super(exMessage);
    }
}
