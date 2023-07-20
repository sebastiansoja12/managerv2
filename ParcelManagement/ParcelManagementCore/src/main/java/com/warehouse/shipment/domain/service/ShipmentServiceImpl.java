package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.ShipmentPaymentException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.ShipmentServicePort;
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



	@Override
	public ShipmentResponse createShipment(ShipmentParcel shipmentParcel) {
		final City city = pathFinderServicePort.determineNewDeliveryDepot(shipmentParcel);
		if (city.getValue() != null) {
			shipmentParcel.setDestination(city.getValue());
		}
		final Parcel parcel = shipmentRepository.save(shipmentParcel);
		final PaymentStatus paymentStatus = paypalServicePort.payment(parcel);

		if (paymentStatus.getLink().isEmpty()) {
			throw new ShipmentPaymentException("URL for payment has not been generated");
		}

		final Notification notification = notificationCreatorProvider.createNotification(parcel,
				paymentStatus.getLink());

		mailServicePort.sendShipmentNotification(notification);

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
}
