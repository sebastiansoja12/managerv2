package com.warehouse.deliveryhandover.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliveryhandover.domain.exception.EmptyDeliveryHandoverException;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;
import com.warehouse.deliveryhandover.domain.vo.RecipientConfirmation;

public class DeliveryHandover {

    static final String MISSING_TRACKING_NUMBER = "Tracking number is required";

    static final String DUPLICATED_TRACKING_NUMBER = "Tracking number is duplicated in request";

    static final String MISSING_SIGNATURE = "Recipient signature is required";

    static final String MISSING_RECIPIENT_NAME = "Recipient name is required when the recipient signs";

    private final ProcessId processId;

    private final List<TrackingNumber> trackingNumbers;

    private final RecipientConfirmation recipientConfirmation;

    public DeliveryHandover(final ProcessId processId,
                            final List<TrackingNumber> trackingNumbers,
                            final RecipientConfirmation recipientConfirmation) {
        if (trackingNumbers == null || trackingNumbers.isEmpty()) {
            throw new EmptyDeliveryHandoverException("Handover request must contain at least one tracking number");
        }
        this.processId = processId;
        this.trackingNumbers = List.copyOf(trackingNumbers);
        this.recipientConfirmation = recipientConfirmation;
    }

    public List<HandoverResult> handOver(final boolean signatureRequired,
                                         final Function<TrackingNumber, HandoverResult> shipmentHandover) {
        final String confirmationError = confirmationError(signatureRequired);
        final Set<TrackingNumber> handledTrackingNumbers = new HashSet<>();
        final List<HandoverResult> results = new ArrayList<>();
        for (final TrackingNumber trackingNumber : trackingNumbers) {
            if (isBlank(trackingNumber)) {
                results.add(HandoverResult.failed(trackingNumber, MISSING_TRACKING_NUMBER));
            } else if (!handledTrackingNumbers.add(trackingNumber)) {
                results.add(HandoverResult.failed(trackingNumber, DUPLICATED_TRACKING_NUMBER));
            } else if (confirmationError != null) {
                results.add(HandoverResult.failed(trackingNumber, confirmationError));
            } else {
                results.add(shipmentHandover.apply(trackingNumber));
            }
        }
        return results;
    }

    public ProcessId processId() {
        return processId;
    }

    public RecipientConfirmation recipientConfirmation() {
        return recipientConfirmation;
    }

    private String confirmationError(final boolean signatureRequired) {
        if (signatureRequired && !recipientConfirmation.signed()) {
            return MISSING_SIGNATURE;
        }
        if (recipientConfirmation.signed() && !recipientConfirmation.hasRecipientName()) {
            return MISSING_RECIPIENT_NAME;
        }
        return null;
    }

    private boolean isBlank(final TrackingNumber trackingNumber) {
        return trackingNumber.value() == null || trackingNumber.value().isBlank();
    }
}
