package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;
import com.warehouse.shipment.domain.port.secondary.CountryRepository;
import com.warehouse.shipment.domain.vo.*;
import org.springframework.stereotype.Service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.CountryDetermineServicePort;


@Service
public class CountryDetermineServiceImpl implements CountryDetermineService {

    private final CountryDetermineServicePort countryDetermineServicePort;

    private final CountryRepository countryRepository;

    public CountryDetermineServiceImpl(final CountryDetermineServicePort countryDetermineServicePort,
                                       final CountryRepository countryRepository) {
        this.countryDetermineServicePort = countryDetermineServicePort;
        this.countryRepository = countryRepository;
    }

    public CountryDetermine determineCountry(final Shipment shipment) {
        final Sender sender = shipment.getSender();
        final Recipient recipient = shipment.getRecipient();

        final LocationInfo locationInfo = LocationInfo.from(sender, recipient);
        final ShipmentCountry shipmentCountry = countryDetermineServicePort.determineCountry(locationInfo);

        return null;
    }

    @Override
    public Result<CountryDetermine, ErrorCode> determineCountry(final ShipmentCreateCommand request) {
        final LocationInfo locationInfo = LocationInfo.from(request.getSender(), request.getRecipient());
        final ShipmentCountry shipmentCountry = countryDetermineServicePort.determineCountry(locationInfo);
        return Result.success();
    }

    @Override
    public Country determineCountryByCode(final CountryCode countryCode) {
        return countryRepository.getCountryNameByCode(countryCode);
    }
}
