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
        shipmentParcel.setDestination(city.getValue());

        if (city.getValue() == null) {
            throw new DestinationDepotDeterminationException("Delivery depot could not be determined");
		}

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
    public Parcel loadParcel(Long parcelId) {
        return shipmentRepository.loadParcelById(parcelId);
    }

    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {
        return shipmentServicePort.update(parcelUpdate);
    }

    @Override
    public void delete(Long parcelId) {
        shipmentRepository.delete(parcelId);
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
