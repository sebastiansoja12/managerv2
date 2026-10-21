package com.warehouse.mail.domain.vo;

import com.warehouse.mail.domain.exception.MailException;

import java.util.Optional;
import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final int MAX_LENGTH = 254;

    public EmailAddress {
        if (!isValid(value)) {
            throw new MailException("E-mail address is invalid");
        }
        value = value.trim();
    }

    public static Optional<EmailAddress> parse(final String value) {
        return isValid(value) ? Optional.of(new EmailAddress(value)) : Optional.empty();
    }

    private static boolean isValid(final String value) {
        return value != null && value.trim().length() <= MAX_LENGTH && EMAIL_PATTERN.matcher(value.trim()).matches();
    }
}
