package com.warehouse.qrcode.domain.vo;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LabelParty(String initials, String city, String postalCode) {

    public static LabelParty of(final String firstName, final String lastName, final String city,
                                final String postalCode) {
        return new LabelParty(initials(firstName, lastName), city, postalCode);
    }

    public static LabelParty unknown() {
        return new LabelParty(null, null, null);
    }

    public String destination() {
        return join(" ", postalCode, city == null ? null : city.toUpperCase(new Locale("pl", "PL")));
    }

    public String origin() {
        return join("  ·  ", initials, city);
    }

    private static String join(final String separator, final String... values) {
        final String joined = Stream.of(values)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(separator));
        return joined.isEmpty() ? null : joined;
    }

    private static String initials(final String firstName, final String lastName) {
        final String initials = Stream.of(firstName, lastName)
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(name -> !name.isEmpty())
                .map(name -> name.substring(0, 1).toUpperCase(Locale.ROOT) + ".")
                .collect(Collectors.joining(" "));
        return initials.isEmpty() ? null : initials;
    }
}
