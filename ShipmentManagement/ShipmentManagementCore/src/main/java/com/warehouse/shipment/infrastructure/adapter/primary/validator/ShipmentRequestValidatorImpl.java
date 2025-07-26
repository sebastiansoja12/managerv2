package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.warehouse.shipment.domain.service.PriceService;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;

public class ShipmentRequestValidatorImpl implements ShipmentRequestValidator {

    private final PriceService priceService;

    public ShipmentRequestValidatorImpl(final PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public void validateBody(final ShipmentCreateRequestDto request) {
        validateRequest(request);

        final List<String> errors = new ArrayList<>();

        if (Objects.isNull(request.sender()) || Objects.isNull(request.recipient())) {
            errors.add("Sender and/or recipient cannot be null");
		} else {
			errors.addAll(validatePerson(request.sender()));
            errors.addAll(validatePerson(request.recipient()));
		}

        if (Objects.isNull(request.shipmentSize())) {
            errors.add("Shipment size cannot be empty");
        } else {
            validateShipmentStatus(request.shipmentSize(), errors);
        }

        if (request.price() == null || request.price().getAmount() == null || request.price().getCurrency() == null) {
            errors.add("Invalid price");
        }

        if (!errors.isEmpty()) {
            throw new ShipmentValidationException(errors, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateShipmentPrice(final MoneyDto price) {
        return false;
    }


    private List<String> validatePerson(final PersonDto person) {
        final Set<String> errors = new HashSet<>();

        if (StringUtils.isEmpty(person.getFirstName())) {
            errors.add("First name is required");
        }

        if (StringUtils.isEmpty(person.getLastName())) {
            errors.add("Last name is required");
        }

        if (StringUtils.isEmpty(person.getEmail())) {
            errors.add("Email is required");
        }

        String telephone = person.getTelephoneNumber();
        if (StringUtils.isEmpty(telephone)) {
            errors.add("Telephone number for sender/recipient is required");
        } else {
            if (telephone.length() != 9) {
                errors.add("Telephone number must be exactly 9 digits");
            }
            if (!Pattern.matches("[0-9]+", telephone)) {
                errors.add("Telephone number must contain only digits");
            }
        }

        if (StringUtils.isEmpty(person.getCity())) {
            errors.add("City is required");
        }

        if (StringUtils.isEmpty(person.getStreet())) {
            errors.add("Street is required");
        }

        if (StringUtils.isEmpty(person.getPostalCode())) {
            errors.add("Postal code is required");
        }

        return errors.isEmpty() ? Collections.emptyList() : new ArrayList<>(errors);
    }


    @Override
    public void validateBody(final ShipmentUpdateRequestDto shipmentRequest) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ShipmentIdDto shipmentId) {
        validateRequest(shipmentId);
        validateValue(shipmentId);
    }

    @Override
    public void validateBody(final ShipmentStatusRequestDto shipmentStatusRequest) {

    }

    @Override
    public void validateBody(final SignatureChangeRequestDto signatureChangeRequest) {

    }

    private void validateShipmentStatus(final ShipmentSizeDto shipmentSize, final List<String> errors) {
        if (Objects.isNull(shipmentSize)) {
            errors.add("Shipment size cannot be empty");
        } else if (ShipmentSizeDto.TEST.equals(shipmentSize)) {
            errors.add("Shipment size not allowed");
        }
    }

    private void validateRequest(final Object obj) {
        if (Objects.isNull(obj)) {
            throw new EmptyRequestException("Request cannot be null");
        }
    }

    private void validateValue(final ShipmentIdDto shipmentId) {
        if (Objects.isNull(shipmentId.getValue())) {
            throw new EmptyRequestException("Value cannot be null");
        }
    }
}
