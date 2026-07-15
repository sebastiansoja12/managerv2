package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

public record OpeningInterval(int startMinute, int endMinute) {

    public OpeningInterval {
        if (startMinute < 0 || startMinute >= 1440) {
            throw new InvalidPickupPointConfigurationException(
                    "Opening interval start minute must be between 0 and 1439");
        }
        if (endMinute < 1 || endMinute > 1440) {
            throw new InvalidPickupPointConfigurationException(
                    "Opening interval end minute must be between 1 and 1440");
        }
        if (startMinute >= endMinute) {
            throw new InvalidPickupPointConfigurationException(
                    "Opening interval must end after it starts");
        }
    }

    public boolean contains(final int minuteOfDay) {
        return minuteOfDay >= this.startMinute && minuteOfDay < this.endMinute;
    }
}
