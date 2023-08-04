package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.exception.ShipmentPaymentException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.vo.Notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentServicePort shipmentServicePort;

    private final ShipmentRepository shipmentRepository;

    private final PathFinderServicePort pathFinderServicePort;

    private final PaypalServicePort paypalServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;
    
    private final MailServicePort mailServicePort;

    private final Logger logger;

	@Override
	public ShipmentResponse createShipment(ShipmentParcel shipmentParcel) {

		final City city = pathFinderServicePort.determineDeliveryDepot(shipmentParcel);

        if (city.getValue() == null) {
            throw new DestinationDepotDeterminationException("Delivery depot could not be determined");
		}

        updateParcelDestination(shipmentParcel, city);

        final Parcel parcel = shipmentRepository.save(shipmentParcel);

        logParcel(parcel);

		final PaymentStatus paymentStatus = paypalServicePort.payment(parcel);

        logPayment(paymentStatus, parcel);

		if (paymentStatus.getLink().isEmpty()) {
			throw new ShipmentPaymentException("URL for payment has not been generated");
		}

		final Notification notification = notificationCreatorProvider.createNotification(parcel,
				paymentStatus.getLink());

		mailServicePort.sendShipmentNotification(notification);

        logNotification(notification);

		return shipmentServicePort.registerParcel(parcel.getId(), paymentStatus.getLink());
	}

    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {
        //final City city = pathFinderServicePort.determineDeliveryDepot(parcelUpdate);
        final City city = new City(null);
        if (city.getValue() == null) {
            setPreviousDepartmentForDelivery(parcelUpdate, city);
        }
        updateParcelDestinationForReroute(parcelUpdate, city);

        final Parcel parcel = shipmentRepository.update(parcelUpdate);

        return new UpdateParcelResponse(parcel);
    }

    @Override
    public Parcel loadParcel(Long parcelId) {
        return shipmentRepository.loadParcelById(parcelId);
    }

    @Override
    public void delete(Long parcelId) {
        shipmentRepository.delete(parcelId);
    }

    private void setPreviousDepartmentForDelivery(ParcelUpdate parcelUpdate, City city) {
        city.setValue(parcelUpdate.getDestination());
    }

    private void updateParcelDestination(ShipmentParcel shipmentParcel, City city) {
        shipmentParcel.setDestination(city.getValue());
    }

    private void updateParcelDestinationForReroute(ParcelUpdate parcelUpdate, City city) {
        parcelUpdate.setDestination(city.getValue());
    }

    private void logNotification(Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logParcel(Parcel parcel) {
        logger.info("Parcel {} has been created", parcel.getId());
    }

    private void logPayment(PaymentStatus status, Parcel parcel) {
        logger.info("Detected payment for parcel {} with payment method {}", parcel.getId(), status.getPaymentMethod());
    }
}
