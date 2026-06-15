package com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet;

public class InvalidDeliveryNetworkSpreadsheetException extends RuntimeException {

    public InvalidDeliveryNetworkSpreadsheetException(final String message) {
        super(message);
    }

    public InvalidDeliveryNetworkSpreadsheetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
