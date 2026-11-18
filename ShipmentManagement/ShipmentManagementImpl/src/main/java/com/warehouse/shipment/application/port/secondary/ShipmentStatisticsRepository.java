package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentDeliveryFigures;
import com.warehouse.shipment.application.port.primary.result.ShipmentRegistrationFigures;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShipmentStatisticsRepository {

    List<ShipmentRegistrationFigures> registrationFigures(final LocalDateTime rangeStart, final LocalDateTime rangeEnd);

    List<ShipmentDeliveryFigures> deliveryFigures(final LocalDateTime rangeStart, final LocalDateTime rangeEnd);

    List<DailyShipmentCount> dailyRegistrations(final LocalDateTime rangeStart, final LocalDateTime rangeEnd);

    Optional<LocalDateTime> firstRegistration();
}
