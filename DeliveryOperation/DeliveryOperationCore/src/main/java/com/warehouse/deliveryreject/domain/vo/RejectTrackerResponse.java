package com.warehouse.deliveryreject.domain.vo;

import java.util.List;
import java.util.Objects;

public record RejectTrackerResponse(List<RejectTrackerResponseDetails> rejectTrackerResponseDetails) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final RejectTrackerResponse that = (RejectTrackerResponse) o;
        return Objects.equals(rejectTrackerResponseDetails, that.rejectTrackerResponseDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rejectTrackerResponseDetails);
    }
}
