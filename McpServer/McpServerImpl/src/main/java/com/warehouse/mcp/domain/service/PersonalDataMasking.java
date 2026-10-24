package com.warehouse.mcp.domain.service;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.warehouse.mcp.domain.vo.MaskedShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentParty;

public class PersonalDataMasking {

    private static final int VISIBLE_PHONE_DIGITS = 3;
    private static final String MASK = "***";

    public MaskedShipmentParty mask(final ShipmentParty party) {
        if (party == null) {
            return null;
        }
        return new MaskedShipmentParty(
                initials(party.firstName(), party.lastName()),
                maskEmail(party.email()),
                maskTelephoneNumber(party.telephoneNumber()),
                party.city(),
                party.postalCode(),
                party.countryCode());
    }

    String initials(final String firstName, final String lastName) {
        final String initials = Stream.of(firstName, lastName)
                .filter(name -> name != null && !name.isBlank())
                .map(name -> name.trim().substring(0, 1).toUpperCase(Locale.ROOT) + ".")
                .collect(Collectors.joining(" "));
        return initials.isEmpty() ? null : initials;
    }

    String maskEmail(final String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        final int separator = email.indexOf('@');
        if (separator <= 0) {
            return MASK;
        }
        return email.charAt(0) + MASK + email.substring(separator);
    }

    String maskTelephoneNumber(final String telephoneNumber) {
        if (telephoneNumber == null || telephoneNumber.isBlank()) {
            return null;
        }
        final String digits = telephoneNumber.replaceAll("\\D", "");
        if (digits.length() <= VISIBLE_PHONE_DIGITS) {
            return MASK;
        }
        return MASK + digits.substring(digits.length() - VISIBLE_PHONE_DIGITS);
    }
}
