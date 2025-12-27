package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.service.PriceService;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.SignatureValidationException;

public class ShipmentRequestValidatorImpl implements ShipmentRequestValidator {

    private final PriceService priceService;

    public ShipmentRequestValidatorImpl(final PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public void validateBody(final ShipmentCreateRequestApi request) {
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

        if (validateShipmentPrice(request.price())) {
            errors.add("Invalid price");
        }

        try {
            CountryCode.valueOf(request.issuerCountryCode());
            CountryCode.valueOf(request.receiverCountryCode());
        } catch (final IllegalArgumentException e) {
            errors.add("Invalid country code");
        }

        if (!errors.isEmpty()) {
            throw new ShipmentValidationException(errors, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateShipmentPrice(final MoneyApi price) {
        return price != null && price.getAmount() == null && price.getCurrency() == null;
    }

    private List<String> validatePerson(final PersonApi person) {
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
    public void validateBody(final ShipmentUpdateRequestApi shipmentRequest) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ShipmentIdDto shipmentId) {
        validateRequest(shipmentId);
        validateValue(shipmentId);
    }

    @Override
    public void validateBody(final ShipmentStatusRequestApi shipmentStatusRequest) {

    }

    @Override
    public void validateBody(final SignatureChangeRequestApi signatureChangeRequest) {
        final List<String> errors = new ArrayList<>();
        errors.addAll(validateShipment(signatureChangeRequest.shipmentId()));
        errors.addAll(validateSignerName(signatureChangeRequest.signerName()));
        errors.addAll(validateDocumentReference(signatureChangeRequest.documentReference()));
        errors.addAll(validateSignature(signatureChangeRequest.signature()));

        if (!errors.isEmpty()) {
            throw new SignatureValidationException(errors, HttpStatus.BAD_REQUEST);
        }
    }

    private Collection<String> validateShipment(final ShipmentIdDto shipmentId) {
        final Set<String> errors = new HashSet<>();
        if (shipmentId == null || shipmentId.getValue() == null) {
            errors.add("Shipment Id is required");
        }
        return errors.isEmpty() ? Collections.emptyList() : new ArrayList<>(errors);
    }

    private Collection<String> validateSignerName(final String s) {
        final Set<String> errors = new HashSet<>();
        if (StringUtils.isEmpty(s)) {
            errors.add("Signer name is required");
        }
        return errors.isEmpty() ? Collections.emptyList() : new ArrayList<>(errors);
    }

    private Collection<String> validateDocumentReference(final String s) {
        final Set<String> errors = new HashSet<>();
        if (StringUtils.isEmpty(s)) {
            errors.add("Document reference is required");
        }
        return errors.isEmpty() ? Collections.emptyList() : new ArrayList<>(errors);
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

    private static List<String> validateSignature(final String signature) {
        final List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(signature)) {
            errors.add("Signature cannot be empty");
        }

        return errors.isEmpty() ? Collections.emptyList() : errors;
    }
}
