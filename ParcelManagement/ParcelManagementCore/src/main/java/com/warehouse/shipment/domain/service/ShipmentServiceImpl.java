package com.warehouse.shipment.domain.service;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;

import java.util.Objects;

import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.vo.ParcelId;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.UpdateParcelResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;
    
    private final MailServicePort mailServicePort;

    private final Logger logger;

    private final RouteLogServicePort routeLogServicePort;

	@Override
	public ShipmentResponse createShipment(ShipmentParcel shipmentParcel) {

        final Address address = buildAddress(shipmentParcel);

		final City city = pathFinderServicePort.determineDeliveryDepot(address);

        if (Objects.isNull(city) || city.getValue() == null) {
            throw new DestinationDepotDeterminationException(SHIPMENT_202);
		}

        updateParcelDestination(shipmentParcel, city);

        final Parcel parcel = shipmentRepository.save(shipmentParcel);

        logParcel(parcel);

		final Notification notification = notificationCreatorProvider.createNotification(parcel);

		mailServicePort.sendShipmentNotification(notification);

        logNotification(notification);

		final RouteProcess routeProcess = routeLogServicePort
				.initializeRouteProcess(ParcelId.builder().value(parcel.getId()).build());

        return new ShipmentResponse(routeProcess.getProcessId().toString(), routeProcess.getParcelId());
	}


    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {

        final Address address = buildAddress(parcelUpdate);

        final City city = pathFinderServicePort.determineDeliveryDepot(address);

        if (!city.getValue().equals(parcelUpdate.getDestination())) {
            updateParcelDestinationForReroute(parcelUpdate, city);
        }

        final Parcel parcel = shipmentRepository.update(parcelUpdate);

        return new UpdateParcelResponse(parcel);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return shipmentRepository.loadParcelById(parcelId);
    }

    @Override
    public boolean exists(Long parcelId) {
        return shipmentRepository.exists(parcelId);
    }

    private Address buildAddress(ShipmentParcel shipmentParcel) {
        return Address.builder()
                .street(shipmentParcel.getRecipient().getStreet())
                .city(shipmentParcel.getRecipient().getCity())
                .postalCode(shipmentParcel.getRecipient().getPostalCode())
                .build();
    }

    private Address buildAddress(ParcelUpdate parcelUpdate) {
        return Address.builder()
                .street(parcelUpdate.getRecipientStreet())
                .city(parcelUpdate.getRecipientCity())
                .postalCode(parcelUpdate.getRecipientPostalCode())
                .build();
    }

    private void updateParcelDestination(ShipmentParcel shipmentParcel, City city) {
        shipmentParcel.setDestination(city.getValue());
    }

    private void updateParcelDestinationForReroute(ParcelUpdate parcelUpdate, City city) {
        if (!Objects.isNull(city) && city.getValue() != null) {
            parcelUpdate.updateDestination(city.getValue());
        }
    }

    private void logNotification(Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logParcel(Parcel parcel) {
        logger.info("Parcel {} has been created at {}", parcel.getId(), parcel.getCreatedAt());
    }
}
