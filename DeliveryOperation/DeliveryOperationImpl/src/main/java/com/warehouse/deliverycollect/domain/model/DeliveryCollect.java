package com.warehouse.deliverycollect.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverycollect.domain.exception.EmptyDeliveryCollectException;
import com.warehouse.deliverycollect.domain.vo.CollectResult;

public class DeliveryCollect {

    static final String MISSING_TRACKING_NUMBER = "Tracking number is required";

    static final String DUPLICATED_TRACKING_NUMBER = "Tracking number is duplicated in request";

    private final ProcessId processId;

    private final List<TrackingNumber> trackingNumbers;

    public DeliveryCollect(final ProcessId processId, final List<TrackingNumber> trackingNumbers) {
        if (trackingNumbers == null || trackingNumbers.isEmpty()) {
            throw new EmptyDeliveryCollectException("Collect request must contain at least one tracking number");
        }
        this.processId = processId;
        this.trackingNumbers = List.copyOf(trackingNumbers);
    }

    public List<CollectResult> collect(final Function<TrackingNumber, CollectResult> shipmentCollector) {
        final Set<TrackingNumber> handledTrackingNumbers = new HashSet<>();
        final List<CollectResult> results = new ArrayList<>();
        for (final TrackingNumber trackingNumber : trackingNumbers) {
            if (isBlank(trackingNumber)) {
                results.add(CollectResult.failed(trackingNumber, MISSING_TRACKING_NUMBER));
            } else if (!handledTrackingNumbers.add(trackingNumber)) {
                results.add(CollectResult.failed(trackingNumber, DUPLICATED_TRACKING_NUMBER));
            } else {
                results.add(shipmentCollector.apply(trackingNumber));
            }
        }
        return results;
    }

    public ProcessId processId() {
        return processId;
    }

    private boolean isBlank(final TrackingNumber trackingNumber) {
        return trackingNumber.value() == null || trackingNumber.value().isBlank();
    }
}
