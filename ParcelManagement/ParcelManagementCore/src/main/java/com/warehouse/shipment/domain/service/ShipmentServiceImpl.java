package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.ShipmentPaymentException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.vo.Notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        // TOBE fixed
		//final City city = pathFinderServicePort.determineNewDeliveryDepot(shipmentParcel);
        final City city = new City("Poznań");
		if (city.getValue() != null) {
			shipmentParcel.setDestination(city.getValue());
		}
		final Parcel parcel = shipmentRepository.save(shipmentParcel);

        logParcel(parcel);

        //TOBE fixed
		//final PaymentStatus paymentStatus = paypalServicePort.payment(parcel);
		final PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setPaymentMethod("paypal");
        paymentStatus.setLink("fake link");

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
        logger.info("Email notification to {0} has been sent", notification.getRecipient());
    }

    private void logParcel(Parcel parcel) {
        logger.info("Parcel {0} has been created", parcel.getId());
    }

    private void logPayment(PaymentStatus status, Parcel parcel) {
        logger.info("Detected payment for parcel {0} with payment method {1}", parcel.getId(), status.getPaymentMethod());
    }
}
