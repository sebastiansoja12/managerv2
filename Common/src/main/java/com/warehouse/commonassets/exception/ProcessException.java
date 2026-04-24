package com.warehouse.commonassets.exception;

public class ProcessException extends RuntimeException {

    private final ProcessFailureDetails processFailureDetails;

    public ProcessException(final ProcessFailureDetails processFailureDetails) {
        super(processFailureDetails.getExceptionMessage());
        this.processFailureDetails = processFailureDetails;
    }

    public ProcessFailureDetails getProcessFailureDetails() {
        return processFailureDetails;
    }
}
