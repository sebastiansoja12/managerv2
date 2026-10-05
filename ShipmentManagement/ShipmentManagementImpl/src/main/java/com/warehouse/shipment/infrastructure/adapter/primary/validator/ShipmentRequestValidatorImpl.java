package com.warehouse.shipment.infrastructure.adapter.primary.validator;

import com.warehouse.shipment.application.service.PriceService;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.SignatureValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.regex.Pattern;

public class ShipmentRequestValidatorImpl implements ShipmentRequestValidator {

    private final PriceService priceService;

    public ShipmentRequestValidatorImpl(final PriceService priceService) {
        this.priceService = priceService;
    }

    private void validateRequest(final ShipmentCreateRequestApi request) {
        validateRequestObj(request);

        final List<String> errors = new ArrayList<>();

        if (Objects.isNull(request.sender()) || Objects.isNull(request.recipient())) {
            errors.add("Sender and/or recipient cannot be null");
		} else {
			errors.addAll(validatePerson(request.sender()));
            errors.addAll(validatePerson(request.recipient()));
		}

        if (request.dimensions() == null) {
            errors.add("Dimensions are required");
        } else {
            validatePositive(request.dimensions().length(), "Length", errors);
            validatePositive(request.dimensions().width(), "Width", errors);
            validatePositive(request.dimensions().height(), "Height", errors);
            if (request.dimensions().unit() == null) {
                errors.add("Dimensions unit is required");
            }
        }

        if (request.weight() == null) {
            errors.add("Weight is required");
        } else {
            validatePositive(request.weight().value(), "Weight", errors);
            if (request.weight().unit() == null) {
                errors.add("Weight unit is required");
            }
        }

        if (request.customerReference() != null && StringUtils.isBlank(request.customerReference())) {
            errors.add("Customer reference cannot be blank");
        }

        if (validateShipmentPrice(request.price())) {
            errors.add("Invalid price");
        }

        if (request.declaredValue() != null && validateShipmentPrice(request.declaredValue())) {
            errors.add("Invalid declared value");
        }

        if (request.deliveryMethod() != null
                && request.deliveryMethod() != DeliveryMethodDto.COURIER
                && request.deliveryPickupPointId() == null) {
            errors.add("Delivery pickup point is required for the selected delivery method");
        }

        if (!errors.isEmpty()) {
            throw new ShipmentValidationException(errors, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateShipmentPrice(final MoneyApi price) {
        return price == null || price.getAmount() == null || StringUtils.isBlank(price.getCurrency());
    }

    private void validatePositive(final java.math.BigDecimal value, final String fieldName,
                                  final List<String> errors) {
        if (value == null || value.signum() <= 0) {
            errors.add(fieldName + " must be greater than 0");
        }
    }

    private List<String> validatePerson(final PersonApi person) {
        final Set<String> errors = new HashSet<>();

        if (StringUtils.isEmpty(person.firstName())) {
            errors.add("First name is required");
        }

        if (StringUtils.isEmpty(person.lastName())) {
            errors.add("Last name is required");
        }

        if (StringUtils.isEmpty(person.email())) {
            errors.add("Email is required");
        }

        String telephone = person.telephoneNumber();
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

        if (StringUtils.isEmpty(person.city())) {
            errors.add("City is required");
        }

        if (StringUtils.isEmpty(person.street())) {
            errors.add("Street is required");
        }

        if (StringUtils.isEmpty(person.postalCode())) {
            errors.add("Postal code is required");
        }

        if (person.countryCode() == null) {
            errors.add("Country code is required");
        }

        return errors.isEmpty() ? Collections.emptyList() : new ArrayList<>(errors);
    }


    @Override
    public void validateRequest(final ShipmentCreateRequestApi shipmentRequest, final ShipmentValidationRules validationRules) {
        validateRequest(shipmentRequest);
    }

    @Override
    public void validateBody(final ShipmentIdDto shipmentId) {
        validateRequestObj(shipmentId);
        validateValue(shipmentId);
    }

    @Override
    public void validateBody(final ShipmentStatusRequestApi shipmentStatusRequest) {
        validateRequestObj(shipmentStatusRequest);
        final List<String> errors = new ArrayList<>();
        errors.addAll(validateShipment(shipmentStatusRequest.shipmentId()));
        if (shipmentStatusRequest.shipmentStatus() == null) {
            errors.add("Shipment status is required");
        }
        if (!errors.isEmpty()) {
            throw new ShipmentValidationException(errors, HttpStatus.BAD_REQUEST);
        }
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

    private void validateRequestObj(final Object obj) {
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
