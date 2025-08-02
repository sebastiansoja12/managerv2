package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.*;
import org.springframework.stereotype.Service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.CountryDetermineServicePort;


@Service
public class CountryDetermineServiceImpl implements CountryDetermineService {

    private final CountryDetermineServicePort countryDetermineServicePort;

    public CountryDetermineServiceImpl(final CountryDetermineServicePort countryDetermineServicePort) {
        this.countryDetermineServicePort = countryDetermineServicePort;
    }


    public CountryDetermine determineCountry(final Shipment shipment) {
        final Country originCountry = shipment.getOriginCountry();
        final Country destinationCountry = shipment.getDestinationCountry();
        final Sender sender = shipment.getSender();
        final Recipient recipient = shipment.getRecipient();

        final LocationInfo locationInfo = LocationInfo.from(sender, recipient);
        final ShipmentCountry shipmentCountry = countryDetermineServicePort.determineCountry(locationInfo);

        return null;
    }

    @Override
    public Result<CountryDetermine, ShipmentErrorCode> determineCountry(final ShipmentCreateRequest request) {
        final LocationInfo locationInfo = LocationInfo.from(request.getSender(), request.getRecipient());
        final ShipmentCountry shipmentCountry = countryDetermineServicePort.determineCountry(locationInfo);
        return Result.success();
    }
}
