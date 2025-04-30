package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.vo.LocationInfo;
import org.springframework.stereotype.Service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.CountryDetermineServicePort;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountry;


@Service
public class CountryDetermineServiceImpl implements CountryDetermineService {

    private final CountryDetermineServicePort countryDetermineServicePort;

    public CountryDetermineServiceImpl(final CountryDetermineServicePort countryDetermineServicePort) {
        this.countryDetermineServicePort = countryDetermineServicePort;
    }

    @Override
    public void determineCountry(final Shipment shipment) {
        final Country originCountry = shipment.getOriginCountry();
        final Country destinationCountry = shipment.getDestinationCountry();
        final Sender sender = shipment.getSender();
        final Recipient recipient = shipment.getRecipient();

        final LocationInfo locationInfo = LocationInfo.from(sender, recipient);
        final ShipmentCountry shipmentCountry = countryDetermineServicePort.determineCountry(locationInfo);

        if (originCountry == null && destinationCountry == null) {
            shipment.changeShipmentCountries(shipmentCountry);
        } else if (originCountry != null && destinationCountry == null) {
            shipment.changeShipmentDestinationCountry(shipmentCountry.destinationCountry());
        } else if (originCountry == null) {
            shipment.changeShipmentOrigin(shipmentCountry.originCountry());
        }
    }
}
