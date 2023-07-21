package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.ShipmentPaymentException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.ShipmentServicePort;
import com.warehouse.shipment.domain.vo.Notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentServicePort shipmentServicePort;

    private final ShipmentRepository shipmentRepository;

    private final PathFinderServicePort pathFinderServicePort;

    private final PaypalServicePort paypalServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;
    
    private final MailServicePort mailServicePort;

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

    private void logPayment(PaymentStatus status, Parcel parcel) {
        log.info("Detected payment for parcel {0} with payment method {1}", parcel.getId(), status.getPaymentMethod());
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
        log.info("Email notification to {0} has been sent", notification.getRecipient());
    }

    private void logParcel(Parcel parcel) {
        log.info("Parcel {0} has been created", parcel.getId());
    }
}
